package ir.ac.kntu.hotelbookingapp.response;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void customConstructor_ShouldCreateValidJwtResponse() {
        // Arrange
        Long id = 1L;
        String email = "test@example.com";
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        // Act
        JwtResponse response = new JwtResponse(id, email, token, roles);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(email, response.getEmail());
        assertEquals(token, response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(roles, response.getRoles());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyJwtResponse() {
        // Act
        JwtResponse response = new JwtResponse();

        // Assert
        assertNull(response.getId());
        assertNull(response.getEmail());
        assertNull(response.getToken());
        assertEquals("Bearer", response.getType());
        assertNull(response.getRoles());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        JwtResponse response = new JwtResponse();
        Long id = 1L;
        String email = "test@example.com";
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String type = "Bearer";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        // Act
        response.setId(id);
        response.setEmail(email);
        response.setToken(token);
        response.setType(type);
        response.setRoles(roles);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(email, response.getEmail());
        assertEquals(token, response.getToken());
        assertEquals(type, response.getType());
        assertEquals(roles, response.getRoles());
    }

    @Test
    void defaultType_ShouldBeBearer() {
        // Act
        JwtResponse response = new JwtResponse();

        // Assert
        assertEquals("Bearer", response.getType());
    }
}
