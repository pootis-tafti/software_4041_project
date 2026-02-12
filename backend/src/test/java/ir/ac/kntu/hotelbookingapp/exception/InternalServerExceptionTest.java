package ir.ac.kntu.hotelbookingapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InternalServerExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        // Arrange
        String errorMessage = "Internal server error occurred";

        // Act
        InternalServerException exception = new InternalServerException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        // Act
        InternalServerException exception = new InternalServerException("Error");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }
}
