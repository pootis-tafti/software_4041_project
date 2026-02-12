package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.exception.PhotoRetrievalException;
import ir.ac.kntu.hotelbookingapp.exception.ResourceNotFoundException;
import ir.ac.kntu.hotelbookingapp.model.BookedRoom;
import ir.ac.kntu.hotelbookingapp.model.Room;
import ir.ac.kntu.hotelbookingapp.response.BookingResponse;
import ir.ac.kntu.hotelbookingapp.response.RoomResponse;
import ir.ac.kntu.hotelbookingapp.service.BookingService;
import ir.ac.kntu.hotelbookingapp.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

	private final IRoomService   roomService;
	private final BookingService bookingService;

	@PostMapping("/add/new-room")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponse> addNewRoom(
			@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType,
			@RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
		Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
		RoomResponse response = new RoomResponse(savedRoom.getId(),
				savedRoom.getRoomType(),
				savedRoom.getRoomPrice());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/room/types")
	public List<String> getRoomTypes() {
		return roomService.getAllRoomTypes();
	}

	@GetMapping("/all-rooms")
	public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
		List<Room> rooms = roomService.getAllRooms(); List<RoomResponse> roomResponses = getRoomResponses(rooms);
		return ResponseEntity.ok(roomResponses);
	}

	@DeleteMapping("/delete/room/{roomId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId) {
		roomService.deleteRoom(roomId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/update/{roomId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
	                                               @RequestParam(required = false) String roomType,
	                                               @RequestParam(required = false) BigDecimal roomPrice,
	                                               @RequestParam(required = false) MultipartFile photo)
	throws IOException, SQLException {
		byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes()
		                                                      : roomService.getRoomPhotoByRoomId(roomId);
		Blob photoBlob = (photoBytes != null && photoBytes.length > 0) ? new SerialBlob(photoBytes) : null;
		Room theRoom   = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
		RoomResponse response = new RoomResponse(theRoom.getId(), theRoom.getRoomType(), theRoom.getRoomPrice(),
				theRoom.isBooked(), photoBytes);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/room/{roomId}")
	public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
		Optional<Room> theRoom = roomService.getRoomById(roomId); return theRoom.map(room -> {
			RoomResponse roomResponse = getRoomResponse(room); return ResponseEntity.ok(Optional.of(roomResponse));
		}).orElseThrow(() -> new ResourceNotFoundException("Room Not Found"));
	}

	@GetMapping("/available-rooms")
	public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestParam("checkInDate") @DateTimeFormat(
			iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate, @RequestParam("checkOutDate") @DateTimeFormat(
			iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate, @RequestParam(
			"roomType") String roomType) throws SQLException {
		List<Room>         availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
		List<RoomResponse> roomResponses  = getRoomResponses(availableRooms); if (roomResponses.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(roomResponses);
	}

	private List<RoomResponse> getRoomResponses(List<Room> availableRooms) throws SQLException {
		List<RoomResponse> roomResponses = new ArrayList<>(); for (Room room : availableRooms) {
			byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
			if (photoBytes != null && photoBytes.length > 0) {
				String       photoBase64  = Base64.getEncoder().encodeToString(photoBytes);
				RoomResponse roomResponse = getRoomResponse(room); roomResponse.setPhoto(photoBase64);
				roomResponses.add(roomResponse);
			}
		}
		return roomResponses;
	}

	private RoomResponse getRoomResponse(Room room) throws PhotoRetrievalException {
		List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
		List<BookingResponse> bookingsInfo = bookings
				                                     .stream()
				                                     .map(booking -> new BookingResponse(
						                                     booking.getBookingId(),
						                                     booking.getCheckInDate(),
						                                     booking.getCheckOutDate(),
						                                     booking.getBookingConfirmationCode())).toList();
		byte[] photoBytes = null;
		Blob   photoBlob  = room.getPhoto();
		if (photoBlob != null) {
			try {
				photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
			} catch (SQLException e) {
				throw new PhotoRetrievalException("Error retrieving photo");
			}
		}
		return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes,
				bookingsInfo);
	}

	private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingService.getAllBookingsByRoomId(roomId);
	}
}
