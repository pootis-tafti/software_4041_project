package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.InvalidBookingRequestException;
import ir.ac.kntu.hotelbookingapp.model.BookedRoom;
import ir.ac.kntu.hotelbookingapp.model.Room;
import ir.ac.kntu.hotelbookingapp.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingService bookingService;

    private BookedRoom bookedRoom;
    private Room room;
    private static final Long ROOM_ID = 1L;
    private static final String CONFIRMATION_CODE = "ABC123";
    private static final String GUEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(ROOM_ID);
        room.setBookings(new ArrayList<>());

        bookedRoom = new BookedRoom();
        bookedRoom.setBookingId(1L);
        bookedRoom.setCheckInDate(LocalDate.now().plusDays(1));
        bookedRoom.setCheckOutDate(LocalDate.now().plusDays(3));
        bookedRoom.setGuestEmail(GUEST_EMAIL);
        bookedRoom.setGuestFullName("John Doe");
        bookedRoom.setBookingConfirmationCode(CONFIRMATION_CODE);
        bookedRoom.setNumberOfAdults(2);
        bookedRoom.setNumberOfChildren(1);
        bookedRoom.setRoom(room);
    }

    @Test
    void getAllBookingsByRoomId_ShouldReturnBookings() {
        // Arrange
        List<BookedRoom> expectedBookings = List.of(bookedRoom);
        when(bookingRepository.findByRoomId(ROOM_ID)).thenReturn(expectedBookings);

        // Act
        List<BookedRoom> actualBookings = bookingService.getAllBookingsByRoomId(ROOM_ID);

        // Assert
        assertEquals(expectedBookings, actualBookings);
        verify(bookingRepository).findByRoomId(ROOM_ID);
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() {
        // Arrange
        List<BookedRoom> expectedBookings = List.of(bookedRoom);
        when(bookingRepository.findAll()).thenReturn(expectedBookings);

        // Act
        List<BookedRoom> actualBookings = bookingService.getAllBookings();

        // Assert
        assertEquals(expectedBookings, actualBookings);
        verify(bookingRepository).findAll();
    }

    @Test
    void findBookingByConfirmationCode_ShouldReturnBooking() {
        // Arrange
        when(bookingRepository.findByBookingConfirmationCode(CONFIRMATION_CODE)).thenReturn(bookedRoom);

        // Act
        BookedRoom actualBooking = bookingService.findBookingByConfirmationCode(CONFIRMATION_CODE);

        // Assert
        assertEquals(bookedRoom, actualBooking);
        verify(bookingRepository).findByBookingConfirmationCode(CONFIRMATION_CODE);
    }

    @Test
    void saveBooking_WithValidDates_ShouldSaveBooking() {
        // Arrange
        Room room = new Room();
        room.setId(ROOM_ID);
        room.setBookings(new ArrayList<>());

        BookedRoom bookingRequest = new BookedRoom();
        bookingRequest.setCheckInDate(LocalDate.now().plusDays(1));
        bookingRequest.setCheckOutDate(LocalDate.now().plusDays(3));

        when(roomService.getRoomById(ROOM_ID)).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(BookedRoom.class))).thenReturn(bookingRequest);

        // Act
        String confirmationCode = bookingService.saveBooking(ROOM_ID, bookingRequest);

        // Assert
        assertNotNull(confirmationCode);
        verify(bookingRepository).save(any(BookedRoom.class));
    }

    @Test
    void saveBooking_WithInvalidDates_ShouldThrowException() {
        // Arrange
        bookedRoom.setCheckInDate(LocalDate.now().plusDays(3));
        bookedRoom.setCheckOutDate(LocalDate.now().plusDays(1));

        // Act & Assert
        assertThrows(InvalidBookingRequestException.class,
                () -> bookingService.saveBooking(ROOM_ID, bookedRoom));
    }

    @Test
    void saveBooking_WithOverlappingDates_ShouldThrowException() {
        // Arrange
        BookedRoom existingBooking = new BookedRoom();
        existingBooking.setCheckInDate(LocalDate.now().plusDays(1));
        existingBooking.setCheckOutDate(LocalDate.now().plusDays(4));
        room.getBookings().add(existingBooking);

        when(roomService.getRoomById(ROOM_ID)).thenReturn(Optional.of(room));

        // Act & Assert
        assertThrows(InvalidBookingRequestException.class,
                () -> bookingService.saveBooking(ROOM_ID, bookedRoom));
    }

    @Test
    void cancelBooking_ShouldDeleteBooking() {
        // Arrange
        Long bookingId = 1L;

        // Act
        bookingService.cancelBooking(bookingId);

        // Assert
        verify(bookingRepository).deleteById(bookingId);
    }

    @Test
    void getBookingsByUserEmail_ShouldReturnUserBookings() {
        // Arrange
        List<BookedRoom> expectedBookings = List.of(bookedRoom);
        when(bookingRepository.findByGuestEmail(GUEST_EMAIL)).thenReturn(expectedBookings);

        // Act
        List<BookedRoom> actualBookings = bookingService.getBookingsByUserEmail(GUEST_EMAIL);

        // Assert
        assertEquals(expectedBookings, actualBookings);
        verify(bookingRepository).findByGuestEmail(GUEST_EMAIL);
    }
}
