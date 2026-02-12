package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.exception.RoleAlreadyExistException;
import ir.ac.kntu.hotelbookingapp.model.Role;
import ir.ac.kntu.hotelbookingapp.model.User;
import ir.ac.kntu.hotelbookingapp.repository.RoleRepository;
import ir.ac.kntu.hotelbookingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@Override
	@Transactional
	public Role createRole(Role theRole) {
		String roleName = "ROLE_" + theRole.getName().toUpperCase();
		Role role = new Role(roleName);
		if (roleRepository.existsByName(roleName)) {
			throw new RoleAlreadyExistException(theRole.getName() + " already exists");
		}
		return roleRepository.save(role);
	}

	@Override
	@Transactional
	public void deleteRole(Long roleId) {
		this.removeAllUsersFromRole(roleId);
		roleRepository.deleteById(roleId);
	}

	@Override
	@Transactional
	public Role findRoleByName(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	@Transactional
	public User removeUserFromRole(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent() && role.get().getUsers().contains(user.get())) {
			role.get().removeUserFromRole(user.get());
			roleRepository.save(role.get());
			return user.get();
		}
		throw new UsernameNotFoundException("User not found");
	}

	@Override
	@Transactional
	public User assignRoleToUser(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (user.isPresent() && user.get().getRoles().contains(role.get())) {
			throw new RoleAlreadyExistException(user.get().getFirstName() + " is already assigned to " + role.get().getName() + "role");
		}
		if (role.isPresent()) {
			role.get().assignRoleToUser(user.get());
			roleRepository.save(role.get());
		}
		return user.get();
	}

	@Override
	@Transactional
	public Role removeAllUsersFromRole(Long roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		role.get().removeAllUsersFromRole();
		return roleRepository.save(role.get());
	}
}
