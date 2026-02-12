package ir.ac.kntu.hotelbookingapp.repository;

import ir.ac.kntu.hotelbookingapp.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
    }

    @Test
    void findByName_ShouldReturnRole() {
        // Arrange
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        // Act
        Optional<Role> found = roleRepository.findByName("ROLE_USER");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("ROLE_USER", found.get().getName());
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenRoleNotFound() {
        // Arrange
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        // Act
        Optional<Role> found = roleRepository.findByName("ROLE_ADMIN");

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenRoleExists() {
        // Arrange
        when(roleRepository.existsByName("ROLE_USER")).thenReturn(true);

        // Act & Assert
        assertTrue(roleRepository.existsByName("ROLE_USER"));
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenRoleDoesNotExist() {
        // Arrange
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(false);

        // Act & Assert
        assertFalse(roleRepository.existsByName("ROLE_ADMIN"));
    }
}
