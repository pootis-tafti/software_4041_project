package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.model.BookedRoom;

import java.util.List;

public interface IBookingService {
	List<BookedRoom> getAllBookingsByRoomId(Long roomId);

	List<BookedRoom> getAllBookings();

	BookedRoom findBookingByConfirmationCode(String confirmationCode);

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	void cancelBooking(Long bookingId);

	List<BookedRoom> getBookingsByUserEmail(String email);

}
