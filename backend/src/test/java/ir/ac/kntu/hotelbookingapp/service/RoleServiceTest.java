package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.RoleAlreadyExistException;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        role.setUsers(new HashSet<>());

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setRoles(new HashSet<>());
    }

    @Test
    void getRoles_ShouldReturnAllRoles() {
        // Arrange
        List<Role> expectedRoles = List.of(role);
        when(roleRepository.findAll()).thenReturn(expectedRoles);

        // Act
        List<Role> actualRoles = roleService.getRoles();

        // Assert
        assertEquals(expectedRoles, actualRoles);
        verify(roleRepository).findAll();
    }

    @Test
    void createRole_WithNewRole_ShouldSaveRole() {
        // Arrange
        Role newRole = new Role("USER");
        when(roleRepository.existsByName("ROLE_USER")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        // Act
        Role createdRole = roleService.createRole(newRole);

        // Assert
        assertNotNull(createdRole);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_WithExistingRole_ShouldThrowException() {
        // Arrange
        Role existingRole = new Role("ADMIN");
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(true);

        // Act & Assert
        assertThrows(RoleAlreadyExistException.class, () -> roleService.createRole(existingRole));
    }

    @Test
    void deleteRole_ShouldRemoveAllUsersAndDeleteRole() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        roleService.deleteRole(roleId);

        // Assert
        verify(roleRepository).deleteById(roleId);
        verify(roleRepository).save(role);
    }

    @Test
    void findRoleByName_ShouldReturnRole() {
        // Arrange
        String roleName = "ADMIN";
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        // Act
        Role foundRole = roleService.findRoleByName(roleName);

        // Assert
        assertEquals(role, foundRole);
        verify(roleRepository).findByName(roleName);
    }

    @Test
    void removeUserFromRole_WhenUserHasRole_ShouldRemoveUser() {
        // Arrange
        role.getUsers().add(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        User result = roleService.removeUserFromRole(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(roleRepository).save(role);
    }

    @Test
    void removeUserFromRole_WhenUserDoesNotHaveRole_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> roleService.removeUserFromRole(1L, 1L));
    }

    @Test
    void assignRoleToUser_WhenUserDoesNotHaveRole_ShouldAssignRole() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Act
        User result = roleService.assignRoleToUser(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(roleRepository).save(role);
    }

    @Test
    void assignRoleToUser_WhenUserAlreadyHasRole_ShouldThrowException() {
        // Arrange
        user.getRoles().add(role);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Act & Assert
        assertThrows(RoleAlreadyExistException.class,
                () -> roleService.assignRoleToUser(1L, 1L));
    }

    @Test
    void removeAllUsersFromRole_ShouldRemoveAllUsers() {
        // Arrange
        Set<User> users = new HashSet<>();
        users.add(user);
        role.setUsers(users);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role result = roleService.removeAllUsersFromRole(1L);

        // Assert
        assertTrue(result.getUsers().isEmpty());
        verify(roleRepository).save(role);
    }
}
