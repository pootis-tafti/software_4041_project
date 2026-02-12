package ir.ac.kntu.hotelbookingapp.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseTest {

    @Test
    void allArgsConstructor_ShouldCreateFullBookingResponse() {
        // Arrange
        Long id = 1L;
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);
        String guestName = "John Doe";
        String guestEmail = "john@example.com";
        int adults = 2;
        int children = 1;
        int totalGuests = 3;
        String confirmationCode = "ABC123";
        RoomResponse room = new RoomResponse();

        // Act
        BookingResponse response = new BookingResponse(
                id, checkIn, checkOut, guestName, guestEmail,
                adults, children, totalGuests, confirmationCode, room
        );

        // Assert
        assertEquals(id, response.getId());
        assertEquals(checkIn, response.getCheckInDate());
        assertEquals(checkOut, response.getCheckOutDate());
        assertEquals(guestName, response.getGuestName());
        assertEquals(guestEmail, response.getGuestEmail());
        assertEquals(adults, response.getNumberOfAdults());
        assertEquals(children, response.getNumberOfChildren());
        assertEquals(totalGuests, response.getTotalNumberOfGuests());
        assertEquals(confirmationCode, response.getBookingConfirmationCode());
        assertEquals(room, response.getRoom());
    }

    @Test
    void customConstructor_ShouldCreatePartialBookingResponse() {
        // Arrange
        Long id = 1L;
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);
        String confirmationCode = "ABC123";

        // Act
        BookingResponse response = new BookingResponse(id, checkIn, checkOut, confirmationCode);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(checkIn, response.getCheckInDate());
        assertEquals(checkOut, response.getCheckOutDate());
        assertEquals(confirmationCode, response.getBookingConfirmationCode());
        assertNull(response.getGuestName());
        assertNull(response.getGuestEmail());
        assertEquals(0, response.getNumberOfAdults());
        assertEquals(0, response.getNumberOfChildren());
        assertEquals(0, response.getTotalNumberOfGuests());
        assertNull(response.getRoom());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyBookingResponse() {
        // Act
        BookingResponse response = new BookingResponse();

        // Assert
        assertNull(response.getId());
        assertNull(response.getCheckInDate());
        assertNull(response.getCheckOutDate());
        assertNull(response.getGuestName());
        assertNull(response.getGuestEmail());
        assertEquals(0, response.getNumberOfAdults());
        assertEquals(0, response.getNumberOfChildren());
        assertEquals(0, response.getTotalNumberOfGuests());
        assertNull(response.getBookingConfirmationCode());
        assertNull(response.getRoom());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        BookingResponse response = new BookingResponse();
        Long id = 1L;
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);
        String guestName = "John Doe";
        String guestEmail = "john@example.com";
        int adults = 2;
        int children = 1;
        int totalGuests = 3;
        String confirmationCode = "ABC123";
        RoomResponse room = new RoomResponse();

        // Act
        response.setId(id);
        response.setCheckInDate(checkIn);
        response.setCheckOutDate(checkOut);
        response.setGuestName(guestName);
        response.setGuestEmail(guestEmail);
        response.setNumberOfAdults(adults);
        response.setNumberOfChildren(children);
        response.setTotalNumberOfGuests(totalGuests);
        response.setBookingConfirmationCode(confirmationCode);
        response.setRoom(room);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(checkIn, response.getCheckInDate());
        assertEquals(checkOut, response.getCheckOutDate());
        assertEquals(guestName, response.getGuestName());
        assertEquals(guestEmail, response.getGuestEmail());
        assertEquals(adults, response.getNumberOfAdults());
        assertEquals(children, response.getNumberOfChildren());
        assertEquals(totalGuests, response.getTotalNumberOfGuests());
        assertEquals(confirmationCode, response.getBookingConfirmationCode());
        assertEquals(room, response.getRoom());
    }
}
