package ir.ac.kntu.hotelbookingapp.response;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomResponseTest {

    private static final Long ID = 1L;
    private static final String ROOM_TYPE = "Deluxe";
    private static final BigDecimal ROOM_PRICE = new BigDecimal("200.00");
    private static final byte[] PHOTO_BYTES = "test photo".getBytes();

    @Test
    void basicConstructor_ShouldCreateBasicRoomResponse() {
        // Act
        RoomResponse response = new RoomResponse(ID, ROOM_TYPE, ROOM_PRICE);

        // Assert
        assertEquals(ID, response.getId());
        assertEquals(ROOM_TYPE, response.getRoomType());
        assertEquals(ROOM_PRICE, response.getRoomPrice());
        assertFalse(response.isBooked());
        assertNull(response.getPhoto());
        assertNull(response.getBookings());
    }

    @Test
    void fullConstructor_WithBookings_ShouldCreateCompleteRoomResponse() {
        // Arrange
        List<BookingResponse> bookings = Arrays.asList(new BookingResponse(), new BookingResponse());
        String expectedPhotoString = Base64.getEncoder().encodeToString(PHOTO_BYTES);

        // Act
        RoomResponse response = new RoomResponse(ID, ROOM_TYPE, ROOM_PRICE, true, PHOTO_BYTES, bookings);

        // Assert
        assertEquals(ID, response.getId());
        assertEquals(ROOM_TYPE, response.getRoomType());
        assertEquals(ROOM_PRICE, response.getRoomPrice());
        assertTrue(response.isBooked());
        assertEquals(expectedPhotoString, response.getPhoto());
        assertEquals(bookings, response.getBookings());
    }

    @Test
    void constructor_WithoutBookings_ShouldCreatePartialRoomResponse() {
        // Arrange
        String expectedPhotoString = Base64.getEncoder().encodeToString(PHOTO_BYTES);

        // Act
        RoomResponse response = new RoomResponse(ID, ROOM_TYPE, ROOM_PRICE, true, PHOTO_BYTES);

        // Assert
        assertEquals(ID, response.getId());
        assertEquals(ROOM_TYPE, response.getRoomType());
        assertEquals(ROOM_PRICE, response.getRoomPrice());
        assertTrue(response.isBooked());
        assertEquals(expectedPhotoString, response.getPhoto());
        assertNull(response.getBookings());
    }

    @Test
    void constructor_WithNullPhoto_ShouldHandleNullPhoto() {
        // Act
        RoomResponse response = new RoomResponse(ID, ROOM_TYPE, ROOM_PRICE, true, null);

        // Assert
        assertNull(response.getPhoto());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyResponse() {
        // Act
        RoomResponse response = new RoomResponse();

        // Assert
        assertNull(response.getId());
        assertNull(response.getRoomType());
        assertNull(response.getRoomPrice());
        assertFalse(response.isBooked());
        assertNull(response.getPhoto());
        assertNull(response.getBookings());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        RoomResponse response = new RoomResponse();
        List<BookingResponse> bookings = Arrays.asList(new BookingResponse(), new BookingResponse());

        // Act
        response.setId(ID);
        response.setRoomType(ROOM_TYPE);
        response.setRoomPrice(ROOM_PRICE);
        response.setBooked(true);
        response.setPhoto(Base64.getEncoder().encodeToString(PHOTO_BYTES));
        response.setBookings(bookings);

        // Assert
        assertEquals(ID, response.getId());
        assertEquals(ROOM_TYPE, response.getRoomType());
        assertEquals(ROOM_PRICE, response.getRoomPrice());
        assertTrue(response.isBooked());
        assertEquals(Base64.getEncoder().encodeToString(PHOTO_BYTES), response.getPhoto());
        assertEquals(bookings, response.getBookings());
    }
}
