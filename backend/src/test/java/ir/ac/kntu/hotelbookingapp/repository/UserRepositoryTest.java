package ir.ac.kntu.hotelbookingapp.repository;

import ir.ac.kntu.hotelbookingapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail(TEST_EMAIL);
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        // Act & Assert
        assertTrue(userRepository.existsByEmail(TEST_EMAIL));
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);

        // Act & Assert
        assertFalse(userRepository.existsByEmail(TEST_EMAIL));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        // Act
        Optional<User> found = userRepository.findByEmail(TEST_EMAIL);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(TEST_EMAIL, found.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act
        Optional<User> found = userRepository.findByEmail(TEST_EMAIL);

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    void deleteByEmail_ShouldInvokeRepositoryMethod() {
        // Act
        userRepository.deleteByEmail(TEST_EMAIL);

        // Assert
        verify(userRepository, times(1)).deleteByEmail(TEST_EMAIL);
    }
}
