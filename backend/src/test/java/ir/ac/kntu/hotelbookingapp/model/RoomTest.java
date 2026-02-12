package ir.ac.kntu.hotelbookingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;
    private BookedRoom bookedRoom;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(new BigDecimal("100.00"));

        bookedRoom = new BookedRoom();
        bookedRoom.setBookingId(1L);
        bookedRoom.setGuestFullName("John Doe");
    }

    @Test
    void defaultConstructorShouldInitializeBookingsList() {
        Room newRoom = new Room();
        assertNotNull(newRoom.getBookings());
        assertTrue(newRoom.getBookings().isEmpty());
    }

    @Test
    void addBookingShouldSetupBookingCorrectly() {
        // Act
        room.addBooking(bookedRoom);

        // Assert
        assertTrue(room.isBooked());
        assertEquals(1, room.getBookings().size());
        assertEquals(room, bookedRoom.getRoom());
        assertNotNull(bookedRoom.getBookingConfirmationCode());
        assertEquals(10, bookedRoom.getBookingConfirmationCode().length());
    }

    @Test
    void addBookingShouldInitializeBookingsListIfNull() {
        // Arrange
        room.setBookings(null);

        // Act
        room.addBooking(bookedRoom);

        // Assert
        assertNotNull(room.getBookings());
        assertEquals(1, room.getBookings().size());
    }

    @Test
    void multipleBookingsShouldBeAddedCorrectly() {
        // Arrange
        BookedRoom secondBooking = new BookedRoom();
        secondBooking.setBookingId(2L);
        secondBooking.setGuestFullName("Jane Doe");

        // Act
        room.addBooking(bookedRoom);
        room.addBooking(secondBooking);

        // Assert
        assertEquals(2, room.getBookings().size());
        assertTrue(room.getBookings().contains(bookedRoom));
        assertTrue(room.getBookings().contains(secondBooking));
    }

    @Test
    void roomFieldsShouldBeSetCorrectly() {
        // Assert
        assertEquals(1L, room.getId());
        assertEquals("Deluxe", room.getRoomType());
        assertEquals(new BigDecimal("100.00"), room.getRoomPrice());
        assertFalse(room.isBooked());
    }
}
