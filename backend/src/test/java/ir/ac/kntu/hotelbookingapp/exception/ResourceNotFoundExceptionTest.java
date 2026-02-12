package ir.ac.kntu.hotelbookingapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        // Arrange
        String errorMessage = "Resource not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException("Error");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }
}
