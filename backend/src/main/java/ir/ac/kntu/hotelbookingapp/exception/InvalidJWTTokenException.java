package ir.ac.kntu.hotelbookingapp.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class InvalidJWTTokenException extends Throwable {
	public InvalidJWTTokenException(String invalidJwtToken, JWTVerificationException e) {
		super(invalidJwtToken, e);
	}
}
