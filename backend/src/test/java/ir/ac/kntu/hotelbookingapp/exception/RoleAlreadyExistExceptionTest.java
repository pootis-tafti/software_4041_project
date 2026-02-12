package ir.ac.kntu.hotelbookingapp.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleAlreadyExistExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        // Arrange
        String errorMessage = "Role already exists";

        // Act
        RoleAlreadyExistException exception = new RoleAlreadyExistException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionShouldBeRuntimeException() {
        // Act
        RoleAlreadyExistException exception = new RoleAlreadyExistException("Error");

        // Assert
        assertInstanceOf(RuntimeException.class, exception);
    }
}
