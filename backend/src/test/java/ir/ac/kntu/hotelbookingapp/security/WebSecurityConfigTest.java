package ir.ac.kntu.hotelbookingapp.security;

import ir.ac.kntu.hotelbookingapp.security.jwt.JwtAuthEntryPoint;
import ir.ac.kntu.hotelbookingapp.security.user.HotelUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @Mock
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Mock
    private HotelUserDetailsService userDetailsService;

    private WebSecurityConfig webSecurityConfig;

    @BeforeEach
    void setUp() {
        webSecurityConfig = new WebSecurityConfig(jwtAuthEntryPoint, userDetailsService);
    }

    @Test
    void authenticationProvider_ShouldCreateDaoAuthenticationProvider() {
        // Act
        DaoAuthenticationProvider provider = webSecurityConfig.authenticationProvider();

        // Assert
        assertNotNull(provider);
    }

    @Test
    void passwordEncoder_ShouldCreateBCryptPasswordEncoder() {
        // Act
        PasswordEncoder encoder = webSecurityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        String encodedPassword = encoder.encode("password");
        assertTrue(encoder.matches("password", encodedPassword));
    }

    @Test
    void corsConfigurationSource_ShouldCreateProperConfiguration() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        // Act
        CorsConfigurationSource source = webSecurityConfig.corsConfigurationSource();
        CorsConfiguration config = source.getCorsConfiguration(request);

        // Assert
        assertNotNull(config);
        assertTrue(Objects.requireNonNull(config.getAllowedOrigins()).contains("http://localhost:5173"));
        assertTrue(Objects.requireNonNull(config.getAllowedMethods()).contains("GET"));
        assertTrue(Objects.requireNonNull(config.getAllowedHeaders()).contains("Authorization"));
        assertEquals(Boolean.TRUE, config.getAllowCredentials());
    }
}
