package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.exception.RoleAlreadyExistException;
import ir.ac.kntu.hotelbookingapp.model.Role;
import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        role = new Role("ROLE_USER");
        role.setId(1L);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void getAllRoles_Success() {
        // Arrange
        List<Role> roles = Collections.singletonList(role);
        when(roleService.getRoles()).thenReturn(roles);

        // Act
        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("ROLE_USER", response.getBody().getFirst().getName());
    }

    @Test
    void createRole_Success() {
        // Arrange
        when(roleService.createRole(any(Role.class))).thenReturn(role);

        // Act
        ResponseEntity<String> response = roleController.createRole(role);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New role created successfully", response.getBody());
    }

    @Test
    void createRole_RoleExists() {
        // Arrange
        when(roleService.createRole(any(Role.class)))
                .thenThrow(new RoleAlreadyExistException("Role already exists"));

        // Act
        ResponseEntity<String> response = roleController.createRole(role);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Role already exists", response.getBody());
    }

    @Test
    void deleteRole_Success() {
        // Act
        roleController.deleteRole(1L);

        // Assert
        verify(roleService, times(1)).deleteRole(1L);
    }

    @Test
    void removeAllUsersFromRole_Success() {
        // Arrange
        when(roleService.removeAllUsersFromRole(1L)).thenReturn(role);

        // Act
        Role result = roleController.removeAllUsersFromRole(1L);

        // Assert
        assertEquals(role, result);
        verify(roleService, times(1)).removeAllUsersFromRole(1L);
    }

    @Test
    void removeUserFromRole_Success() {
        // Arrange
        when(roleService.removeUserFromRole(1L, 1L)).thenReturn(user);

        // Act
        User result = roleController.removeUserFromRole(1L, 1L);

        // Assert
        assertEquals(user, result);
        verify(roleService, times(1)).removeUserFromRole(1L, 1L);
    }

    @Test
    void assignUserToRole_Success() {
        // Arrange
        when(roleService.assignRoleToUser(1L, 1L)).thenReturn(user);

        // Act
        User result = roleController.assignUserToRole(1L, 1L);

        // Assert
        assertEquals(user, result);
        verify(roleService, times(1)).assignRoleToUser(1L, 1L);
    }
}
