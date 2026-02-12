package ir.ac.kntu.hotelbookingapp.security;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private final CorsConfig corsConfig = new CorsConfig();

    @Test
    void corsFilter_ShouldCreateProperConfiguration() throws NoSuchFieldException, IllegalAccessException {
        // Act
        CorsFilter filter = corsConfig.corsFilter();

        // Get the configuration source using reflection
        Field configSourceField = CorsFilter.class.getDeclaredField("configSource");
        configSourceField.setAccessible(true);
        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource) configSourceField.get(filter);

        CorsConfiguration config = source.getCorsConfigurations().get("/**");

        // Assert
        assertNotNull(config);
        assertTrue(Objects.requireNonNull(config.getAllowedOrigins()).contains("*"));
        assertTrue(Objects.requireNonNull(config.getAllowedMethods()).contains("*"));
        assertTrue(Objects.requireNonNull(config.getAllowedHeaders()).contains("*"));
        assertEquals(Boolean.TRUE, config.getAllowCredentials());
    }
}
