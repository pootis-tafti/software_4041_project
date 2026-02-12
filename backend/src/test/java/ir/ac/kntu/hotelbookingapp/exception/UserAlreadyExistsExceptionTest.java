package ir.ac.kntu.hotelbookingapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        // Arrange
        String errorMessage = "User already exists";

        // Act
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        // Act
        UserAlreadyExistsException exception = new UserAlreadyExistsException("Error");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }
}
