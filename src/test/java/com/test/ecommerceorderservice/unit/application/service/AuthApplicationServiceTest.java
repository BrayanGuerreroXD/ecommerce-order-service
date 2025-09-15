package com.test.ecommerceorderservice.unit.application.service;

import com.test.ecommerceorderservice.application.dto.request.LoginRequest;
import com.test.ecommerceorderservice.application.dto.response.LoginResponse;
import com.test.ecommerceorderservice.application.service.AuthApplicationService;
import com.test.ecommerceorderservice.infrastructure.security.service.AuthService;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthApplicationService authApplicationService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void login_success_returnsLoginResponse() {
        // Arrange
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("secret");

        LoginResponse expected = new LoginResponse(
                "user@example.com",
                "jwt-token"
        );

        when(authService.login("user@example.com", "secret")).thenReturn(expected);

        // Act
        LoginResponse actual = authApplicationService.login(req);

        // Assert
        assertNotNull(actual);
        assertEquals(expected.getToken(), actual.getToken());
        assertEquals(expected.getEmail(), actual.getEmail());
        verify(authService, times(1)).login("user@example.com", "secret");
    }

    @Test
    void login_whenAuthServiceThrows_propagatesException() {
        // Arrange
        LoginRequest req = new LoginRequest();
        req.setEmail("bad@example.com");
        req.setPassword("badpwd");

        when(authService.login(anyString(), anyString()))
                .thenThrow(new IllegalStateException("Auth failed"));

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> authApplicationService.login(req));
        assertEquals("Auth failed", ex.getMessage());
        verify(authService).login("bad@example.com", "badpwd");
    }

    @Test
    void closeSession_success_returnsSuccessResponse() {
        // Arrange
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        SuccessResponse expected = new SuccessResponse();
        expected.setSuccess(true);

        when(authService.closeSession(servletRequest)).thenReturn(expected);

        // Act
        SuccessResponse actual = authApplicationService.closeSession(servletRequest);

        // Assert
        assertNotNull(actual);
        assertEquals("Session closed", actual.isSuccess() ? "Session closed" : "Session not closed");
        verify(authService, times(1)).closeSession(servletRequest);
    }

    @Test
    void closeSession_whenAuthServiceReturnsNull_handlesNullGracefully() {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(authService.closeSession(servletRequest)).thenReturn(null);

        SuccessResponse actual = authApplicationService.closeSession(servletRequest);

        assertNull(actual, "If AuthService returns null, the result should be null");
        verify(authService, times(1)).closeSession(servletRequest);
    }
}
