package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.UserAlreadyExistsException;
import ir.ac.kntu.hotelbookingapp.model.Role;
import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.repository.RoleRepository;
import ir.ac.kntu.hotelbookingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword123";

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName("ROLE_USER");

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    void registerUser_WithNewUser_ShouldRegisterSuccessfully() {
        // Arrange
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User registeredUser = userService.registerUser(user);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(EMAIL, registeredUser.getEmail());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(PASSWORD);
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getUsers();

        // Assert
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository).findAll();
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(EMAIL);

        // Assert
        verify(userRepository).deleteByEmail(EMAIL);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail(anyString()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userService.deleteUser("nonexistent@example.com"));
        verify(userRepository, never()).deleteByEmail(anyString());
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.getUser(EMAIL);

        // Assert
        assertNotNull(foundUser);
        assertEquals(EMAIL, foundUser.getEmail());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void getUser_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUser("nonexistent@example.com"));
    }
}
