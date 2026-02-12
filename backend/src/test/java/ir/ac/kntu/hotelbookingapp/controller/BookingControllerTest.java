package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.model.BookedRoom;
import ir.ac.kntu.hotelbookingapp.model.Room;
import ir.ac.kntu.hotelbookingapp.response.BookingResponse;
import ir.ac.kntu.hotelbookingapp.response.RoomResponse;
import ir.ac.kntu.hotelbookingapp.service.IBookingService;
import ir.ac.kntu.hotelbookingapp.service.IRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private IBookingService bookingService;

    @Mock
    private IRoomService roomService;

    @InjectMocks
    private BookingController bookingController;

    private BookedRoom bookedRoom;
    private Room room;
    private RoomResponse roomResponse;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @BeforeEach
    void setUp() {
        checkInDate = LocalDate.now();
        checkOutDate = checkInDate.plusDays(2);

        room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(new BigDecimal("100.00"));

        roomResponse = new RoomResponse(1L, "Deluxe", new BigDecimal("100.00"));

        bookedRoom = new BookedRoom();
        bookedRoom.setBookingId(1L);
        bookedRoom.setRoom(room);
        bookedRoom.setCheckInDate(checkInDate);
        bookedRoom.setCheckOutDate(checkOutDate);
        bookedRoom.setGuestFullName("John Doe");
        bookedRoom.setGuestEmail("john@example.com");
        bookedRoom.setNumberOfAdults(2);
        bookedRoom.setNumberOfChildren(1);
        bookedRoom.setBookingConfirmationCode("ABC123");
    }

    @Test
    void getAllBookings_Success() {
        // Arrange
        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(bookedRoom));
        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(room));

        // Act
        ResponseEntity<List<BookingResponse>> response = bookingController.getAllBookings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        BookingResponse bookingResponse = response.getBody().getFirst();
        assertEquals("John Doe", bookingResponse.getGuestName());
        assertEquals(checkInDate, bookingResponse.getCheckInDate());
        assertEquals(checkOutDate, bookingResponse.getCheckOutDate());
        assertEquals("ABC123", bookingResponse.getBookingConfirmationCode());
        assertEquals(roomResponse.getRoomType(), bookingResponse.getRoom().getRoomType());
    }

    @Test
    void getBookingsByUserEmail_Success() {
        // Arrange
        when(bookingService.getBookingsByUserEmail("john@example.com"))
                .thenReturn(Collections.singletonList(bookedRoom));
        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(room));

        // Act
        ResponseEntity<List<BookingResponse>> response =
                bookingController.getBookingsByUserEmail("john@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookingResponse bookingResponse = Objects.requireNonNull(response.getBody()).getFirst();
        assertEquals("john@example.com", bookingResponse.getGuestEmail());
        assertEquals(3, bookingResponse.getTotalNumberOfGuests());
        assertEquals(roomResponse.getRoomPrice(), bookingResponse.getRoom().getRoomPrice());
    }

    @Test
    void getBookingByConfirmationCode_Success() {
        // Arrange
        when(bookingService.findBookingByConfirmationCode("ABC123")).thenReturn(bookedRoom);
        when(roomService.getRoomById(anyLong())).thenReturn(Optional.of(room));

        // Act
        ResponseEntity<?> response = bookingController.getBookingByConfirmationCode("ABC123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookingResponse bookingResponse = (BookingResponse) response.getBody();
        assert bookingResponse != null;
        assertEquals(1L, bookingResponse.getId());
        assertEquals("ABC123", bookingResponse.getBookingConfirmationCode());
        assertEquals(roomResponse.getId(), bookingResponse.getRoom().getId());
    }
}
