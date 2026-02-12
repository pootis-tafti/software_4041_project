package ir.ac.kntu.hotelbookingapp.security.user;

import ir.ac.kntu.hotelbookingapp.model.Role;
import ir.ac.kntu.hotelbookingapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HotelUserDetailsTests {

    @Test
    void constructor_ShouldCreateValidInstance() {
        // Arrange
        Long id = 1L;
        String email = "test@example.com";
        String password = "password123";
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // Act
        HotelUserDetails userDetails = new HotelUserDetails(id, email, password, authorities);

        // Assert
        assertEquals(id, userDetails.getId());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertEquals(authorities, userDetails.getAuthorities());
    }

    @Test
    void buildUserDetails_ShouldCreateCorrectUserDetails() {
        // Arrange
        Role role = new Role();
        role.setName("ROLE_USER");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRoles(Set.of(role));

        // Act
        HotelUserDetails userDetails = HotelUserDetails.buildUserDetails(user);

        // Assert
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void getUsername_ShouldReturnEmail() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails(
                1L,
                "test@example.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Act & Assert
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void accountStatus_ShouldReturnTrue() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails(
                1L,
                "test@example.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Act & Assert
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyInstance() {
        // Act
        HotelUserDetails userDetails = new HotelUserDetails();

        // Assert
        assertNull(userDetails.getId());
        assertNull(userDetails.getEmail());
        assertNull(userDetails.getPassword());
        assertNull(userDetails.getAuthorities());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        HotelUserDetails userDetails = new HotelUserDetails();
        Long id = 1L;
        String email = "test@example.com";
        String password = "password123";
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // Act
        userDetails.setId(id);
        userDetails.setEmail(email);
        userDetails.setPassword(password);
        userDetails.setAuthorities(authorities);

        // Assert
        assertEquals(id, userDetails.getId());
        assertEquals(email, userDetails.getEmail());
        assertEquals(password, userDetails.getPassword());
        assertEquals(authorities, userDetails.getAuthorities());
    }
}
