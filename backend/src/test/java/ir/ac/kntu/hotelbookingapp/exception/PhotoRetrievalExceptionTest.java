package ir.ac.kntu.hotelbookingapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhotoRetrievalExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        // Arrange
        String errorMessage = "Failed to retrieve photo";

        // Act
        PhotoRetrievalException exception = new PhotoRetrievalException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        // Act
        PhotoRetrievalException exception = new PhotoRetrievalException("Error");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }
}
