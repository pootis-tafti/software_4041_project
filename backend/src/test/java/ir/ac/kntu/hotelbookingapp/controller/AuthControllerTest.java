package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.exception.UserAlreadyExistsException;
import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.request.LoginRequest;
import ir.ac.kntu.hotelbookingapp.response.JwtResponse;
import ir.ac.kntu.hotelbookingapp.security.jwt.JwtUtils;
import ir.ac.kntu.hotelbookingapp.security.user.HotelUserDetails;
import ir.ac.kntu.hotelbookingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private User user;
    private LoginRequest loginRequest;
    private HotelUserDetails userDetails;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        userDetails = new HotelUserDetails(
                1L,
                "test@example.com",
                "password123",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void registerUser_Success() {
        // Arrange
        when(userService.registerUser(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<?> response = authController.registerUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration successful!", response.getBody());
    }

    @Test
    void registerUser_UserAlreadyExists() {
        // Arrange
        when(userService.registerUser(any(User.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        // Act
        ResponseEntity<?> response = authController.registerUser(user);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void login_Success() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtTokenForUser(authentication)).thenReturn("jwt-token");

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("test@example.com", jwtResponse.getEmail());
        assertEquals("jwt-token", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertTrue(jwtResponse.getRoles().contains("ROLE_USER"));
    }
}
