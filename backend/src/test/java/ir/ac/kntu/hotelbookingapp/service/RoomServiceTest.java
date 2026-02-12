package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.ResourceNotFoundException;
import ir.ac.kntu.hotelbookingapp.model.Room;
import ir.ac.kntu.hotelbookingapp.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private MultipartFile multipartFile;
    private static final Long ROOM_ID = 1L;
    private static final String ROOM_TYPE = "Deluxe";
    private static final BigDecimal ROOM_PRICE = new BigDecimal("200.00");

    @BeforeEach
    void setUp() throws SQLException {
        room = new Room();
        room.setId(ROOM_ID);
        room.setRoomType(ROOM_TYPE);
        room.setRoomPrice(ROOM_PRICE);

        byte[] photoBytes = "test photo".getBytes();
        Blob photoBlob = new SerialBlob(photoBytes);
        room.setPhoto(photoBlob);

        multipartFile = new MockMultipartFile(
                "photo",
                "test.jpg",
                "image/jpeg",
                "test photo".getBytes()
        );
    }

    @Test
    void addNewRoom_WithValidData_ShouldCreateRoom() throws IOException, SQLException {
        // Arrange
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room savedRoom = roomService.addNewRoom(multipartFile, ROOM_TYPE, ROOM_PRICE);

        // Assert
        assertNotNull(savedRoom);
        assertEquals(ROOM_TYPE, savedRoom.getRoomType());
        assertEquals(ROOM_PRICE, savedRoom.getRoomPrice());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void getAllRoomTypes_ShouldReturnTypes() {
        // Arrange
        List<String> expectedTypes = List.of(ROOM_TYPE);
        when(roomRepository.findDistinctRoomTypes()).thenReturn(expectedTypes);

        // Act
        List<String> actualTypes = roomService.getAllRoomTypes();

        // Assert
        assertEquals(expectedTypes, actualTypes);
        verify(roomRepository).findDistinctRoomTypes();
    }

    @Test
    void getAllRooms_ShouldReturnAllRooms() {
        // Arrange
        List<Room> expectedRooms = List.of(room);
        when(roomRepository.findAll()).thenReturn(expectedRooms);

        // Act
        List<Room> actualRooms = roomService.getAllRooms();

        // Assert
        assertEquals(expectedRooms, actualRooms);
        verify(roomRepository).findAll();
    }

    @Test
    void getRoomPhotoByRoomId_WhenRoomExists_ShouldReturnPhoto() throws SQLException {
        // Arrange
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));

        // Act
        byte[] photo = roomService.getRoomPhotoByRoomId(ROOM_ID);

        // Assert
        assertNotNull(photo);
        verify(roomRepository).findById(ROOM_ID);
    }

    @Test
    void getRoomPhotoByRoomId_WhenRoomNotFound_ShouldThrowException() {
        // Arrange
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> roomService.getRoomPhotoByRoomId(ROOM_ID));
    }

    @Test
    void deleteRoom_WhenRoomExists_ShouldDelete() {
        // Arrange
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));

        // Act
        roomService.deleteRoom(ROOM_ID);

        // Assert
        verify(roomRepository).deleteById(ROOM_ID);
    }

    @Test
    void updateRoom_WithValidData_ShouldUpdateRoom() {
        // Arrange
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room updatedRoom = roomService.updateRoom(ROOM_ID, "New Type",
                new BigDecimal("300.00"), "new photo".getBytes());

        // Assert
        assertNotNull(updatedRoom);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void getRoomById_WhenRoomExists_ShouldReturnRoom() {
        // Arrange
        when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(room));

        // Act
        Optional<Room> foundRoom = roomService.getRoomById(ROOM_ID);

        // Assert
        assertTrue(foundRoom.isPresent());
        assertEquals(room, foundRoom.get());
    }

    @Test
    void getAvailableRooms_ShouldReturnAvailableRooms() {
        // Arrange
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);
        List<Room> expectedRooms = List.of(room);
        when(roomRepository.findAvailableRoomsByDatesAndType(checkIn, checkOut, ROOM_TYPE))
                .thenReturn(expectedRooms);

        // Act
        List<Room> availableRooms = roomService.getAvailableRooms(checkIn, checkOut, ROOM_TYPE);

        // Assert
        assertEquals(expectedRooms, availableRooms);
        verify(roomRepository).findAvailableRoomsByDatesAndType(checkIn, checkOut, ROOM_TYPE);
    }
}
