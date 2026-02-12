package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.model.Room;
import ir.ac.kntu.hotelbookingapp.response.RoomResponse;
import ir.ac.kntu.hotelbookingapp.service.BookingService;
import ir.ac.kntu.hotelbookingapp.service.IRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private IRoomService roomService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private RoomController roomController;

    private Room room;
    private MultipartFile multipartFile;
    private byte[] photoBytes;

    @BeforeEach
    void setUp() throws Exception {
        photoBytes = "test photo".getBytes();
        room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(new BigDecimal("100.00"));
        room.setPhoto(new SerialBlob(photoBytes));

        multipartFile = new MockMultipartFile(
                "photo",
                "test.jpg",
                "image/jpeg",
                photoBytes
        );
    }

    @Test
    void addNewRoom_Success() throws Exception {
        // Arrange
        when(roomService.addNewRoom(any(), anyString(), any(BigDecimal.class))).thenReturn(room);

        // Act
        ResponseEntity<RoomResponse> response = roomController.addNewRoom(
                multipartFile,
                "Deluxe",
                new BigDecimal("100.00")
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(room.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(room.getRoomType(), response.getBody().getRoomType());
    }

    @Test
    void getRoomTypes_Success() {
        // Arrange
        List<String> roomTypes = Arrays.asList("Deluxe", "Standard");
        when(roomService.getAllRoomTypes()).thenReturn(roomTypes);

        // Act
        List<String> result = roomController.getRoomTypes();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("Deluxe"));
    }

    @Test
    void getAllRooms_Success() throws Exception {
        // Arrange
        when(roomService.getAllRooms()).thenReturn(Collections.singletonList(room));
        when(roomService.getRoomPhotoByRoomId(anyLong())).thenReturn(photoBytes);

        // Act
        ResponseEntity<List<RoomResponse>> response = roomController.getAllRooms();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteRoom_Success() {
        // Act
        ResponseEntity<Void> response = roomController.deleteRoom(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roomService, times(1)).deleteRoom(1L);
    }

    @Test
    void updateRoom_Success() throws Exception {
        // Arrange
        when(roomService.updateRoom(anyLong(), anyString(), any(BigDecimal.class), any(byte[].class)))
                .thenReturn(room);

        // Act
        ResponseEntity<RoomResponse> response = roomController.updateRoom(
                1L,
                "Deluxe Suite",
                new BigDecimal("150.00"),
                multipartFile
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAvailableRooms_Success() throws Exception {
        // Arrange
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        when(roomService.getAvailableRooms(checkIn, checkOut, "Deluxe"))
                .thenReturn(Collections.singletonList(room));
        when(roomService.getRoomPhotoByRoomId(anyLong())).thenReturn(photoBytes);

        // Act
        ResponseEntity<List<RoomResponse>> response = roomController.getAvailableRooms(
                checkIn,
                checkOut,
                "Deluxe"
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getRoomById_Success() {
        // Arrange
        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));
        when(bookingService.getAllBookingsByRoomId(1L)).thenReturn(List.of());

        // Act
        ResponseEntity<Optional<RoomResponse>> response = roomController.getRoomById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isPresent());
    }
}
