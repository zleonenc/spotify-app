package com.example.spotify_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.doThrow;

import java.net.URI;

import com.example.spotify_app.service.SpotifyOAuthService;
import com.example.spotify_app.util.AuthUtils;

@ExtendWith(MockitoExtension.class)
class SpotifyOAuthControllerTest {

    @Mock
    private SpotifyOAuthService oauthService;

    private SpotifyOAuthController spotifyOAuthController;

    private static final String TEST_AUTH_HEADER = "Bearer test-token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_CODE = "test-auth-code";
    private static final String TEST_AUTH_URL = "https://accounts.spotify.com/authorize?test=true";
    private static final String TEST_SUCCESS_URL = "http://localhost:9090/callback?success=true&userId=test-user";
    private static final String TEST_ERROR_URL = "http://localhost:9090/callback?error=true&message=error";

    @BeforeEach
    void setUp() {
        spotifyOAuthController = new SpotifyOAuthController(oauthService);
    }

    // Tests for SpotifyOAuth method

    @Test
    void SpotifyOAuth_ValidRequest_returnsRedirectToSpotify() {
        // Given
        when(oauthService.buildAuthorizationUrl()).thenReturn(TEST_AUTH_URL);

        // When
        ResponseEntity<Void> result = spotifyOAuthController.SpotifyOAuth();

        // Then
        assertEquals(HttpStatus.FOUND, result.getStatusCode());
        assertNotNull(result.getHeaders().getLocation());
        assertEquals(URI.create(TEST_AUTH_URL), result.getHeaders().getLocation());
        verify(oauthService).buildAuthorizationUrl();
    }

    @Test
    void SpotifyOAuth_ServiceThrowsException_returnsInternalServerError() {
        // Given
        when(oauthService.buildAuthorizationUrl()).thenThrow(new RuntimeException("Service error"));

        // When
        ResponseEntity<Void> result = spotifyOAuthController.SpotifyOAuth();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        verify(oauthService).buildAuthorizationUrl();
    }

    // Tests for SpotifyCallback method

    @Test
    void SpotifyCallback_ValidCode_returnsSuccessRedirect() throws Exception {
        // Given
        when(oauthService.exchangeCodeForToken(TEST_CODE)).thenReturn(TEST_USER_ID);
        when(oauthService.buildSuccessRedirectUrl(TEST_USER_ID)).thenReturn(TEST_SUCCESS_URL);

        // When
        ResponseEntity<Void> result = spotifyOAuthController.SpotifyCallback(TEST_CODE);

        // Then
        assertEquals(HttpStatus.FOUND, result.getStatusCode());
        assertEquals(URI.create(TEST_SUCCESS_URL), result.getHeaders().getLocation());
        verify(oauthService).exchangeCodeForToken(TEST_CODE);
        verify(oauthService).buildSuccessRedirectUrl(TEST_USER_ID);
    }

    @Test
    void SpotifyCallback_ExchangeCodeThrowsException_returnsErrorRedirect() throws Exception {
        // Given
        String errorMessage = "Invalid authorization code";
        when(oauthService.exchangeCodeForToken(TEST_CODE)).thenThrow(new RuntimeException(errorMessage));
        when(oauthService.buildErrorRedirectUrl(errorMessage)).thenReturn(TEST_ERROR_URL);

        // When
        ResponseEntity<Void> result = spotifyOAuthController.SpotifyCallback(TEST_CODE);

        // Then
        assertEquals(HttpStatus.FOUND, result.getStatusCode());
        assertEquals(URI.create(TEST_ERROR_URL), result.getHeaders().getLocation());
        verify(oauthService).exchangeCodeForToken(TEST_CODE);
        verify(oauthService).buildErrorRedirectUrl(errorMessage);
    }

    @Test
    void SpotifyCallback_BuildSuccessUrlThrowsException_returnsErrorRedirect() throws Exception {
        // Given
        String errorMessage = "Failed to build success URL";
        when(oauthService.exchangeCodeForToken(TEST_CODE)).thenReturn(TEST_USER_ID);
        when(oauthService.buildSuccessRedirectUrl(TEST_USER_ID)).thenThrow(new RuntimeException(errorMessage));
        when(oauthService.buildErrorRedirectUrl(errorMessage)).thenReturn(TEST_ERROR_URL);

        // When
        ResponseEntity<Void> result = spotifyOAuthController.SpotifyCallback(TEST_CODE);

        // Then
        assertEquals(HttpStatus.FOUND, result.getStatusCode());
        assertEquals(URI.create(TEST_ERROR_URL), result.getHeaders().getLocation());
        verify(oauthService).exchangeCodeForToken(TEST_CODE);
        verify(oauthService).buildSuccessRedirectUrl(TEST_USER_ID);
        verify(oauthService).buildErrorRedirectUrl(errorMessage);
    }

    @Test
    void SpotifyCallback_GenericException_returnsErrorRedirect() throws Exception {
        // Given
        Exception genericException = new Exception("Generic error");
        when(oauthService.exchangeCodeForToken(TEST_CODE)).thenThrow(genericException);
        when(oauthService.buildErrorRedirectUrl("Generic error")).thenReturn(TEST_ERROR_URL);

        // When
        ResponseEntity<Void> result = spotifyOAuthController.SpotifyCallback(TEST_CODE);

        // Then
        assertEquals(HttpStatus.FOUND, result.getStatusCode());
        assertEquals(URI.create(TEST_ERROR_URL), result.getHeaders().getLocation());
        verify(oauthService).exchangeCodeForToken(TEST_CODE);
        verify(oauthService).buildErrorRedirectUrl("Generic error");
    }

    // Tests for logout method

    @Test
    void logout_ValidAuthHeader_returnsOk() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);

            // When
            ResponseEntity<Void> result = spotifyOAuthController.logout(TEST_AUTH_HEADER);

            // Then
            assertEquals(HttpStatus.OK, result.getStatusCode());
            verify(oauthService).logout(TEST_USER_ID);
        }
    }

    @Test
    void logout_InvalidAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(null);

            // When
            ResponseEntity<Void> result = spotifyOAuthController.logout(TEST_AUTH_HEADER);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void logout_NullAuthHeader_returnsUnauthorized() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(null)).thenReturn(null);

            // When
            ResponseEntity<Void> result = spotifyOAuthController.logout(null);

            // Then
            assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        }
    }

    @Test
    void logout_ServiceThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER)).thenReturn(TEST_USER_ID);
            doThrow(new RuntimeException("Logout error")).when(oauthService).logout(TEST_USER_ID);

            // When
            ResponseEntity<Void> result = spotifyOAuthController.logout(TEST_AUTH_HEADER);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
            verify(oauthService).logout(TEST_USER_ID);
        }
    }

    @Test
    void logout_AuthUtilsThrowsException_returnsInternalServerError() {
        // Given
        try (MockedStatic<AuthUtils> authUtilsMock = mockStatic(AuthUtils.class)) {
            authUtilsMock.when(() -> AuthUtils.extractUserId(TEST_AUTH_HEADER))
                    .thenThrow(new RuntimeException("Auth error"));

            // When
            ResponseEntity<Void> result = spotifyOAuthController.logout(TEST_AUTH_HEADER);

            // Then
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        }
    }
}
