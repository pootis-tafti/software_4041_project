package ir.ac.kntu.hotelbookingapp.repository;

import ir.ac.kntu.hotelbookingapp.model.BookedRoom;
import ir.ac.kntu.hotelbookingapp.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    private Room room;
    private BookedRoom bookedRoom;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");

        bookedRoom = new BookedRoom();
        bookedRoom.setBookingId(1L);
        bookedRoom.setCheckInDate(LocalDate.now());
        bookedRoom.setCheckOutDate(LocalDate.now().plusDays(2));
        bookedRoom.setGuestFullName("John Doe");
        bookedRoom.setGuestEmail("john@example.com");
        bookedRoom.setNumberOfAdults(2);
        bookedRoom.setNumberOfChildren(1);
        bookedRoom.setBookingConfirmationCode("ABC123");
        bookedRoom.setRoom(room);
    }

    @Test
    void findByRoomId_ShouldReturnBookings() {
        // Arrange
        when(bookingRepository.findByRoomId(1L)).thenReturn(Collections.singletonList(bookedRoom));

        // Act
        List<BookedRoom> found = bookingRepository.findByRoomId(1L);

        // Assert
        assertEquals(1, found.size());
        assertEquals(room.getId(), found.getFirst().getRoom().getId());
    }

    @Test
    void findByBookingConfirmationCode_ShouldReturnBooking() {
        // Arrange
        when(bookingRepository.findByBookingConfirmationCode("ABC123")).thenReturn(bookedRoom);

        // Act
        BookedRoom found = bookingRepository.findByBookingConfirmationCode("ABC123");

        // Assert
        assertNotNull(found);
        assertEquals("ABC123", found.getBookingConfirmationCode());
        assertEquals("John Doe", found.getGuestFullName());
    }

    @Test
    void findByGuestEmail_ShouldReturnBookings() {
        // Arrange
        when(bookingRepository.findByGuestEmail("john@example.com")).thenReturn(Collections.singletonList(bookedRoom));

        // Act
        List<BookedRoom> found = bookingRepository.findByGuestEmail("john@example.com");

        // Assert
        assertEquals(1, found.size());
        assertEquals("john@example.com", found.getFirst().getGuestEmail());
        assertEquals(3, found.getFirst().getTotalNumberOfGuests());
    }
}
