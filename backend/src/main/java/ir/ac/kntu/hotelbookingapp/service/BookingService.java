package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.InvalidBookingRequestException;
import ir.ac.kntu.hotelbookingapp.model.BookedRoom;
import ir.ac.kntu.hotelbookingapp.model.Room;
import ir.ac.kntu.hotelbookingapp.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService implements IBookingService {
	private final BookingRepository bookingRepository;
	private final RoomService roomService;

	@Autowired
	public BookingService(BookingRepository bookingRepository, RoomService roomService) {
		this.bookingRepository = bookingRepository;
		this.roomService = roomService;
	}

	@Override
	@Transactional
	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	@Transactional
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}

	@Override
	@Transactional
	public BookedRoom findBookingByConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode);
	}

	@Override
	@Transactional
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in data must come before check-out date");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
		if (roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		} else {
			throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
		}
		return bookingRequest.getBookingConfirmationCode();
	}

	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		return existingBookings.stream()
				.noneMatch(existingBooking ->
						bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())

						|| bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())

						|| (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
						&& bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))

						|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
						&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))

						|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
						&& bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

						|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
						&& bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

						|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
						&& bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
				);
	}

	@Override
	@Transactional
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
	}

	@Override
	@Transactional
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}
}
