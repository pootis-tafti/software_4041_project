package ir.ac.kntu.hotelbookingapp.controller;

import ir.ac.kntu.hotelbookingapp.exception.UserAlreadyExistsException;
import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.request.LoginRequest;
import ir.ac.kntu.hotelbookingapp.response.JwtResponse;
import ir.ac.kntu.hotelbookingapp.security.jwt.JwtUtils;
import ir.ac.kntu.hotelbookingapp.security.user.HotelUserDetails;
import ir.ac.kntu.hotelbookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserService           userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils              jwtUtils;

	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			userService.registerUser(user);
			return ResponseEntity.ok("Registration successful!");
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
		Authentication authentication =
				authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
								request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String           jwt         = jwtUtils.generateJwtTokenForUser(authentication);
		HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
		return ResponseEntity.ok(new JwtResponse(
				userDetails.getId(),
				userDetails.getEmail(),
				jwt,
				roles));
	}
}
