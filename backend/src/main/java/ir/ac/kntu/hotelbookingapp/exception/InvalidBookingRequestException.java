package ir.ac.kntu.hotelbookingapp.exception;

public class InvalidBookingRequestException extends RuntimeException{
	public InvalidBookingRequestException(String message) {
		super(message);
	}
}
