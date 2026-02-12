package ir.ac.kntu.hotelbookingapp.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreationAndGetters() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testRolesManagement() {
        // Arrange
        User user = new User();
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        // Act
        Collection<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        user.setRoles(roles);

        // Assert
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(userRole));
        assertTrue(user.getRoles().contains(adminRole));
    }

    @Test
    void testDefaultRolesCollection() {
        // Arrange
        User user = new User();

        // Assert
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testUserFieldUpdates() {
        // Arrange
        User user = new User();
        user.setFirstName("John");
        user.setEmail("john@example.com");

        // Act
        user.setFirstName("Jane");
        user.setEmail("jane@example.com");

        // Assert
        assertEquals("Jane", user.getFirstName());
        assertEquals("jane@example.com", user.getEmail());
    }
}
