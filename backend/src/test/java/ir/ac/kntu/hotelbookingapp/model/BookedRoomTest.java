package ir.ac.kntu.hotelbookingapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BookedRoomTest {

    private BookedRoom bookedRoom;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @BeforeEach
    void setUp() {
        bookedRoom = new BookedRoom();
        room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        checkInDate = LocalDate.now();
        checkOutDate = checkInDate.plusDays(2);
    }

    @Test
    void calculateTotalNumberOfGuests_ShouldSumAdultsAndChildren() {
        // Act
        bookedRoom.setNumberOfAdults(2);
        bookedRoom.setNumberOfChildren(3);

        // Assert
        assertEquals(5, bookedRoom.getTotalNumberOfGuests());
    }

    @Test
    void setNumberOfAdults_ShouldUpdateTotalGuests() {
        // Arrange
        bookedRoom.setNumberOfChildren(2);

        // Act
        bookedRoom.setNumberOfAdults(3);

        // Assert
        assertEquals(5, bookedRoom.getTotalNumberOfGuests());
    }

    @Test
    void setNumberOfChildren_ShouldUpdateTotalGuests() {
        // Arrange
        bookedRoom.setNumberOfAdults(2);

        // Act
        bookedRoom.setNumberOfChildren(1);

        // Assert
        assertEquals(3, bookedRoom.getTotalNumberOfGuests());
    }

    @Test
    void allArgsConstructor_ShouldSetAllFields() {
        // Arrange & Act
        BookedRoom room = new BookedRoom(
                1L, checkInDate, checkOutDate, "John Doe",
                "john@example.com", 2, 1, 3,
                "ABC123", this.room
        );

        // Assert
        assertEquals(1L, room.getBookingId());
        assertEquals(checkInDate, room.getCheckInDate());
        assertEquals(checkOutDate, room.getCheckOutDate());
        assertEquals("John Doe", room.getGuestFullName());
        assertEquals("john@example.com", room.getGuestEmail());
        assertEquals(2, room.getNumberOfAdults());
        assertEquals(1, room.getNumberOfChildren());
        assertEquals(3, room.getTotalNumberOfGuests());
        assertEquals("ABC123", room.getBookingConfirmationCode());
        assertEquals(this.room, room.getRoom());
    }

    @Test
    void setRoom_ShouldUpdateRoomReference() {
        // Act
        bookedRoom.setRoom(room);

        // Assert
        assertEquals(room, bookedRoom.getRoom());
    }

    @Test
    void setBookingConfirmationCode_ShouldUpdateCode() {
        // Act
        bookedRoom.setBookingConfirmationCode("XYZ789");

        // Assert
        assertEquals("XYZ789", bookedRoom.getBookingConfirmationCode());
    }
}
