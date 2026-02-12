package ir.ac.kntu.hotelbookingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        role = new Role("ROLE_USER");
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void constructorShouldSetName() {
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void assignRoleToUserShouldAddUserAndRole() {
        // Act
        role.assignRoleToUser(user);

        // Assert
        assertTrue(role.getUsers().contains(user));
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    void removeUserFromRoleShouldRemoveUserAndRole() {
        // Arrange
        role.assignRoleToUser(user);

        // Act
        role.removeUserFromRole(user);

        // Assert
        assertFalse(role.getUsers().contains(user));
        assertFalse(user.getRoles().contains(role));
    }

    @Test
    void removeAllUsersFromRoleShouldClearAllUsers() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        role.assignRoleToUser(user1);
        role.assignRoleToUser(user2);

        // Act
        role.removeAllUsersFromRole();

        // Assert
        assertTrue(role.getUsers().isEmpty());
        assertFalse(user1.getRoles().contains(role));
        assertFalse(user2.getRoles().contains(role));
    }

    @Test
    void getNameShouldReturnEmptyStringWhenNull() {
        // Arrange
        Role emptyRole = new Role();

        // Act & Assert
        assertEquals("", emptyRole.getName());
    }

    @Test
    void defaultConstructorShouldInitializeUsersCollection() {
        // Arrange
        Role newRole = new Role();

        // Assert
        assertNotNull(newRole.getUsers());
        assertTrue(newRole.getUsers().isEmpty());
    }
}
