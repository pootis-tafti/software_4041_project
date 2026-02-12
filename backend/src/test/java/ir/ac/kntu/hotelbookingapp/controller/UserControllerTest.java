package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    void getUsers_Success() {
        // Arrange
        List<User> users = Collections.singletonList(user);
        when(userService.getUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.getUsers();

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("test@example.com", response.getBody().getFirst().getEmail());
    }

    @Test
    void getUserByEmail_Success() {
        // Arrange
        when(userService.getUser("test@example.com")).thenReturn(user);

        // Act
        ResponseEntity<?> response = userController.getUserByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User returnedUser = (User) response.getBody();
        assert returnedUser != null;
        assertEquals("test@example.com", returnedUser.getEmail());
    }

    @Test
    void getUserByEmail_UserNotFound() {
        // Arrange
        when(userService.getUser("nonexistent@example.com"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = userController.getUserByEmail("nonexistent@example.com");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void getUserByEmail_InternalServerError() {
        // Arrange
        when(userService.getUser("test@example.com"))
                .thenThrow(new RuntimeException("Internal server error"));

        // Act
        ResponseEntity<?> response = userController.getUserByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }

    @Test
    void deleteUser_Success() {
        // Arrange
        doNothing().when(userService).deleteUser("test@example.com");

        // Act
        ResponseEntity<String> response = userController.deleteUser("test@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser("test@example.com");
    }

    @Test
    void deleteUser_UserNotFound() {
        // Arrange
        doThrow(new UsernameNotFoundException("User not found"))
                .when(userService).deleteUser("nonexistent@example.com");

        // Act
        ResponseEntity<String> response = userController.deleteUser("nonexistent@example.com");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void deleteUser_InternalServerError() {
        // Arrange
        doThrow(new RuntimeException("Internal server error"))
                .when(userService).deleteUser("test@example.com");

        // Act
        ResponseEntity<String> response = userController.deleteUser("test@example.com");

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}
