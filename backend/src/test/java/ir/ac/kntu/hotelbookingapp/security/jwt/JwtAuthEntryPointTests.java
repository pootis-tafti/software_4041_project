package ir.ac.kntu.hotelbookingapp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthEntryPointTest {

    private JwtAuthEntryPoint jwtAuthEntryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException authException;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jwtAuthEntryPoint = new JwtAuthEntryPoint();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authException = mock(AuthenticationException.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    void commence_ShouldReturnUnauthorizedResponse() throws IOException {
        // Arrange
        String testPath = "/api/test";
        String errorMessage = "Unauthorized access";
        ((MockHttpServletRequest) request).setServletPath(testPath);
        when(authException.getMessage()).thenReturn(errorMessage);

        // Act
        jwtAuthEntryPoint.commence(request, response, authException);

        // Assert
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        String responseContent = ((MockHttpServletResponse) response).getContentAsString();
        Map responseMap = objectMapper.readValue(responseContent, Map.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseMap.get("status"));
        assertEquals(errorMessage, responseMap.get("message"));
        assertEquals(testPath, responseMap.get("path"));
    }

    @Test
    void commence_ShouldHandleEmptyPath() throws IOException {
        // Arrange
        String errorMessage = "Access denied";
        when(authException.getMessage()).thenReturn(errorMessage);

        // Act
        jwtAuthEntryPoint.commence(request, response, authException);

        // Assert
        String responseContent = ((MockHttpServletResponse) response).getContentAsString();
        Map responseMap = objectMapper.readValue(responseContent, Map.class);

        assertEquals("", responseMap.get("path"));
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseMap.get("status"));
        assertEquals(errorMessage, responseMap.get("message"));
    }

    @Test
    void commence_ShouldHandleNullErrorMessage() throws IOException {
        // Arrange
        when(authException.getMessage()).thenReturn(null);

        // Act
        jwtAuthEntryPoint.commence(request, response, authException);

        // Assert
        String responseContent = ((MockHttpServletResponse) response).getContentAsString();
        Map responseMap = objectMapper.readValue(responseContent, Map.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseMap.get("status"));
        assertNull(responseMap.get("message"));
    }
}
