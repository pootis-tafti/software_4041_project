package ir.ac.kntu.hotelbookingapp.service;

import ir.ac.kntu.hotelbookingapp.model.Role;
import ir.ac.kntu.hotelbookingapp.model.User;

import java.util.List;

public interface IRoleService {
	List<Role> getRoles();

	Role createRole(Role theRole);

	void deleteRole(Long id);

	Role findRoleByName(String name);

	User removeUserFromRole(Long userId, Long roleId);

	User assignRoleToUser(Long userId, Long roleId);

	Role removeAllUsersFromRole(Long roleId);
}
