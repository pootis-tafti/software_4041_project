package ir.ac.kntu.hotelbookingapp.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123";

    @Test
    void testGettersAndSetters() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();

        // Act
        loginRequest.setEmail(VALID_EMAIL);
        loginRequest.setPassword(VALID_PASSWORD);

        // Assert
        assertEquals(VALID_EMAIL, loginRequest.getEmail());
        assertEquals(VALID_PASSWORD, loginRequest.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        LoginRequest request1 = new LoginRequest();
        LoginRequest request2 = new LoginRequest();

        request1.setEmail(VALID_EMAIL);
        request1.setPassword(VALID_PASSWORD);
        request2.setEmail(VALID_EMAIL);
        request2.setPassword(VALID_PASSWORD);

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(VALID_EMAIL);
        loginRequest.setPassword(VALID_PASSWORD);

        // Act
        String toString = loginRequest.toString();

        // Assert
        assertTrue(toString.contains(VALID_EMAIL));
        assertTrue(toString.contains(VALID_PASSWORD));
    }
}
