package ir.ac.kntu.hotelbookingapp.security.jwt;

import ir.ac.kntu.hotelbookingapp.security.user.HotelUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private static final String SECRET = "testSecretKey123testSecretKey123testSecretKey123";
    private static final int EXPIRATION_TIME = 3600000; // 1 hour
    private static final String EMAIL = "test@example.com";
    private static final String ROLE = "ROLE_USER";
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", Base64.getEncoder().encodeToString(SECRET.getBytes()));
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationTime", EXPIRATION_TIME);
    }

    @Test
    void generateJwtTokenForUser_ShouldGenerateValidToken() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails(
                USER_ID,
                EMAIL,
                "password",
                List.of(new SimpleGrantedAuthority(ROLE))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // Act
        String token = jwtUtils.generateJwtTokenForUser(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails(
                USER_ID,
                EMAIL,
                "password",
                List.of(new SimpleGrantedAuthority(ROLE))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        String token = jwtUtils.generateJwtTokenForUser(authentication);

        // Act & Assert
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Act & Assert
        assertFalse(jwtUtils.validateToken("invalid.token.here"));
    }

    @Test
    void getUserNameFromToken_WithValidToken_ShouldReturnUsername() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails(
                USER_ID,
                EMAIL,
                "password",
                List.of(new SimpleGrantedAuthority(ROLE))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        String token = jwtUtils.generateJwtTokenForUser(authentication);

        // Act
        String extractedEmail = jwtUtils.getUserNameFromToken(token);

        // Assert
        assertEquals(EMAIL, extractedEmail);
    }

    @Test
    void getUserNameFromToken_WithInvalidToken_ShouldReturnNull() {
        // Act
        String username = jwtUtils.getUserNameFromToken("invalid.token.here");

        // Assert
        assertNull(username);
    }

    @Test
    void getRolesFromToken_WithValidToken_ShouldReturnRoles() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails(
                USER_ID,
                EMAIL,
                "password",
                List.of(new SimpleGrantedAuthority(ROLE))
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        String token = jwtUtils.generateJwtTokenForUser(authentication);

        // Act
        List<String> roles = jwtUtils.getRolesFromToken(token);

        // Assert
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(ROLE, roles.getFirst());
    }

    @Test
    void getRolesFromToken_WithInvalidToken_ShouldReturnNull() {
        // Act
        List<String> roles = jwtUtils.getRolesFromToken("invalid.token.here");

        // Assert
        assertNull(roles);
    }
}
