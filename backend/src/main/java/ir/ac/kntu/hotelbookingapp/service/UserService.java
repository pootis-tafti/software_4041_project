package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.UserAlreadyExistsException;
import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.model.Role;
import ir.ac.kntu.hotelbookingapp.repository.RoleRepository;
import ir.ac.kntu.hotelbookingapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	private static final Logger          logger = LoggerFactory.getLogger(UserService.class);
	private final        UserRepository  userRepository;
	private final        PasswordEncoder passwordEncoder;
	private final        RoleRepository  roleRepository;

	@Override
	@Transactional
	public User registerUser(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException(user.getEmail() + " already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role userRole = null;
		try {
			userRole
					= roleRepository.findByName("ROLE_USER")
							  .orElseThrow(() -> new RoleNotFoundException("Role not found"));
		} catch (RoleNotFoundException e) {
			logger.error("Role not found");
		}
		user.setRoles(Collections.singletonList(userRole));
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteUser(String email) {
		User theUser = getUser(email);
		if (theUser != null) {
			userRepository.deleteByEmail(email);
		}
	}

	@Override
	@Transactional
	public User getUser(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
