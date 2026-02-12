package ir.ac.kntu.hotelbookingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long   id;

	@Column(length = 50, unique = true)
	private String name;

	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private Collection<User> users = new HashSet<>();

	public void assignRoleToUser(User user) {
		user.getRoles().add(this);
		this.users.add(user);
	}

	public Role(String name) {
		this.name = name;
	}

	public void removeUserFromRole(User user) {
		user.getRoles().remove(this);
		this.users.remove(user);
	}

	public void removeAllUsersFromRole() {
		if (this.getUsers() != null) {
			List<User> roleUsers = this.getUsers().stream().toList();
			roleUsers.forEach(this::removeUserFromRole);
		}
	}

	public String getName() {
		return name != null ? name : "";
	}
}
