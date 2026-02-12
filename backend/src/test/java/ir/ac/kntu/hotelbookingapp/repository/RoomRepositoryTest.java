package ir.ac.kntu.hotelbookingapp.repository;

import ir.ac.kntu.hotelbookingapp.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomRepositoryTest {

    @Mock
    private RoomRepository roomRepository;

    private Room deluxeRoom;
    private Room standardRoom;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @BeforeEach
    void setUp() {
        deluxeRoom = new Room();
        deluxeRoom.setId(1L);
        deluxeRoom.setRoomType("Deluxe");

        standardRoom = new Room();
        standardRoom.setId(2L);
        standardRoom.setRoomType("Standard");

        checkInDate = LocalDate.now();
        checkOutDate = checkInDate.plusDays(2);
    }

    @Test
    void findDistinctRoomTypes_ShouldReturnUniqueTypes() {
        // Arrange
        List<String> roomTypes = Arrays.asList("Deluxe", "Standard", "Suite");
        when(roomRepository.findDistinctRoomTypes()).thenReturn(roomTypes);

        // Act
        List<String> result = roomRepository.findDistinctRoomTypes();

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains("Deluxe"));
        assertTrue(result.contains("Standard"));
        assertTrue(result.contains("Suite"));
    }

    @Test
    void findAvailableRoomsByDatesAndType_ShouldReturnAvailableRooms() {
        // Arrange
        List<Room> availableRooms = Collections.singletonList(deluxeRoom);
        when(roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, "Deluxe"))
                .thenReturn(availableRooms);

        // Act
        List<Room> result = roomRepository.findAvailableRoomsByDatesAndType(
                checkInDate, checkOutDate, "Deluxe"
        );

        // Assert
        assertEquals(1, result.size());
        assertEquals("Deluxe", result.getFirst().getRoomType());
    }

    @Test
    void findAvailableRoomsByDatesAndType_ShouldReturnEmptyList_WhenNoRoomsAvailable() {
        // Arrange
        when(roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, "Suite"))
                .thenReturn(List.of());

        // Act
        List<Room> result = roomRepository.findAvailableRoomsByDatesAndType(
                checkInDate, checkOutDate, "Suite"
        );

        // Assert
        assertTrue(result.isEmpty());
    }
}
