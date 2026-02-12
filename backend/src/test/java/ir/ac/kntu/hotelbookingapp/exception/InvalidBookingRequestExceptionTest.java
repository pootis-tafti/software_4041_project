package ir.ac.kntu.hotelbookingapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidBookingRequestExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        // Arrange
        String errorMessage = "Invalid booking request";

        // Act
        InvalidBookingRequestException exception = new InvalidBookingRequestException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        // Act
        InvalidBookingRequestException exception = new InvalidBookingRequestException("Error");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }
}
