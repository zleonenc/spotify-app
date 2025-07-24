package com.example.spotify_app.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;

import com.example.spotify_app.config.RetryConfig;
import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;
import com.example.spotify_app.util.RetryUtils;

@ExtendWith(MockitoExtension.class)
class SpotifyApiClientTest {

    @Mock
    private TokenStore tokenStore;

    @Mock
    private SpotifyConfig spotifyConfig;

    @Mock
    private RetryConfig retryConfig;

    @Mock
    private RetryUtils retryUtils;

    @Mock
    private SpotifyOAuthService oauthService;

    private SpotifyApiClient spotifyApiClient;

    private static final String TEST_USER_ID = "test_user_id";
    private static final String TEST_ACCESS_TOKEN = "test_access_token";
    private static final String TEST_API_ENDPOINT = "/v1/me/profile";
    private static final String TEST_API_URL = "https://api.spotify.com";

    @BeforeEach
    void setUp() {
        spotifyApiClient = new SpotifyApiClient(tokenStore, spotifyConfig, retryConfig, retryUtils, oauthService);
    }

    // Tests for getValidAccessToken method

    @Test
    void getValidAccessToken_ValidUserIdWithToken_returnsAccessToken() {
        // Given
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.setAccessToken(TEST_ACCESS_TOKEN);
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(tokenResponse);

        // When
        String result = spotifyApiClient.getValidAccessToken(TEST_USER_ID);

        // Then
        assertEquals(TEST_ACCESS_TOKEN, result);
        verify(tokenStore).getToken(TEST_USER_ID);
    }

    @Test
    void getValidAccessToken_ValidUserIdNoToken_returnsNull() {
        // Given
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(null);

        // When
        String result = spotifyApiClient.getValidAccessToken(TEST_USER_ID);

        // Then
        assertNull(result);
        verify(tokenStore).getToken(TEST_USER_ID);
    }

    @Test
    void getValidAccessToken_ValidUserIdNullAccessToken_returnsNull() {
        // Given
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.setAccessToken(null);
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(tokenResponse);

        // When
        String result = spotifyApiClient.getValidAccessToken(TEST_USER_ID);

        // Then
        assertNull(result);
        verify(tokenStore).getToken(TEST_USER_ID);
    }

    @Test
    void getValidAccessToken_ValidUserIdEmptyAccessToken_returnsNull() {
        // Given
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.setAccessToken("");
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(tokenResponse);

        // When
        String result = spotifyApiClient.getValidAccessToken(TEST_USER_ID);

        // Then
        assertNull(result);
        verify(tokenStore).getToken(TEST_USER_ID);
    }

    // Tests for makeRequest method

    @Test
    void makeRequest_ValidUserIdNullToken_returnsUnauthorized() {
        // Given
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(null);

        // When
        ResponseEntity<String> result = spotifyApiClient.makeRequest(TEST_USER_ID, TEST_API_ENDPOINT, String.class);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(tokenStore).getToken(TEST_USER_ID);
    }

    @Test
    void makeRequest_ValidUserIdEmptyAccessToken_returnsUnauthorized() {
        // Given
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.setAccessToken("");
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(tokenResponse);

        // When
        ResponseEntity<String> result = spotifyApiClient.makeRequest(TEST_USER_ID, TEST_API_ENDPOINT, String.class);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(tokenStore).getToken(TEST_USER_ID);
    }

    @Test
    void makeRequest_ValidInputWithConfiguration_invokesAllRequiredServices() {
        // Given
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.setAccessToken(TEST_ACCESS_TOKEN);
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(tokenResponse);
        when(spotifyConfig.getApiUrl()).thenReturn(TEST_API_URL);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);

        // When
        ResponseEntity<String> result = spotifyApiClient.makeRequest(TEST_USER_ID, TEST_API_ENDPOINT, String.class);

        // Then - Verify all service interactions
        verify(tokenStore).getToken(TEST_USER_ID);
        verify(spotifyConfig).getApiUrl();
        verify(retryConfig).getMaxRetryAttempts();
        assertNotNull(result);
        assertNotNull(result.getStatusCode());
    }

    @Test
    void makeRequest_TokenRefreshSetting_handlesRefreshLogic() {
        // Given
        SpotifyTokenResponse oldTokenResponse = new SpotifyTokenResponse();
        oldTokenResponse.setAccessToken(TEST_ACCESS_TOKEN);
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(oldTokenResponse);
        when(oauthService.refreshToken(TEST_USER_ID)).thenReturn(true);
        when(spotifyConfig.getApiUrl()).thenReturn(TEST_API_URL);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);

        // When
        ResponseEntity<String> result = spotifyApiClient.makeRequest(TEST_USER_ID, TEST_API_ENDPOINT, String.class);

        // Then
        verify(tokenStore, atLeastOnce()).getToken(TEST_USER_ID);
        assertNotNull(result);
        assertNotNull(result.getStatusCode());
    }

    @Test
    void makeRequest_NullUserId_returnsUnauthorized() {
        // When
        ResponseEntity<String> result = spotifyApiClient.makeRequest(null, TEST_API_ENDPOINT, String.class);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(tokenStore).getToken(null);
    }

    @Test
    void makeRequest_NullApiEndpoint_returnsBadRequest() {
        // Given
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse();
        tokenResponse.setAccessToken(TEST_ACCESS_TOKEN);
        when(tokenStore.getToken(TEST_USER_ID)).thenReturn(tokenResponse);

        // When
        ResponseEntity<String> result = spotifyApiClient.makeRequest(TEST_USER_ID, null, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        verify(tokenStore).getToken(TEST_USER_ID);
    }
}
