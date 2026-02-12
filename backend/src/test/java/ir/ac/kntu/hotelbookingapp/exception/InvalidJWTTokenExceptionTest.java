package ir.ac.kntu.hotelbookingapp.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidJWTTokenExceptionTest {

    @Test
    void constructorShouldSetMessageAndCause() {
        // Arrange
        String errorMessage = "Invalid JWT token";
        JWTVerificationException cause = new JWTVerificationException("Token verification failed");

        // Act
        InvalidJWTTokenException exception = new InvalidJWTTokenException(errorMessage, cause);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void exceptionShouldBeThrowable() {
        // Arrange
        JWTVerificationException cause = new JWTVerificationException("Verification failed");

        // Act
        InvalidJWTTokenException exception = new InvalidJWTTokenException("Invalid token", cause);

        // Assert
        assertInstanceOf(Throwable.class, exception);
    }

    @Test
    void causeTypeShouldBeJWTVerificationException() {
        // Arrange
        JWTVerificationException cause = new JWTVerificationException("Token expired");

        // Act
        InvalidJWTTokenException exception = new InvalidJWTTokenException("Invalid token", cause);

        // Assert
        assertInstanceOf(JWTVerificationException.class, exception.getCause());
    }
}
