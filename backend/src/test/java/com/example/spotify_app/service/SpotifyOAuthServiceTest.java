package com.example.spotify_app.service;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

import com.example.spotify_app.config.SpotifyConfig;
import com.example.spotify_app.config.RetryConfig;
import com.example.spotify_app.model.SpotifyTokenResponse;
import com.example.spotify_app.util.RetryUtils;
import com.example.spotify_app.util.UserIdGenerator;

@ExtendWith(MockitoExtension.class)
class SpotifyOAuthServiceTest {

    @Mock
    private SpotifyConfig spotifyConfig;

    @Mock
    private RetryConfig retryConfig;

    @Mock
    private RetryUtils retryUtils;

    @Mock
    private TokenStore tokenStore;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private SpotifyOAuthService spotifyOAuthService;

    private static final String TEST_USER_ID = "test_user_id";
    private static final String TEST_CLIENT_ID = "test_client_id";
    private static final String TEST_CLIENT_SECRET = "test_client_secret";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String FRONTEND_URL = "http://localhost:9090";
    private static final String SCOPES = "user-read-private user-read-email";

    @BeforeEach
    void setUp() {
        spotifyOAuthService = new SpotifyOAuthService(spotifyConfig, retryConfig, retryUtils, tokenStore);
    }

    @Test
    void buildAuthorizationUrl_returnsFormattedUrl() {
        // Given
        when(spotifyConfig.getAuthorizeUrl()).thenReturn("https://accounts.spotify.com/authorize");
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getRedirectUri()).thenReturn(REDIRECT_URI);
        when(spotifyConfig.getScopes()).thenReturn(SCOPES);

        // When
        String result = spotifyOAuthService.buildAuthorizationUrl();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("https://accounts.spotify.com/authorize"));
        assertTrue(result.contains("client_id=test_client_id"));
        assertTrue(result.contains("response_type=code"));
        assertTrue(result.contains("redirect_uri="));
        assertTrue(result.contains("scope="));
    }

    @Test
    void buildSuccessRedirectUrl_ValidUserId_returnsFormattedUrl() {
        // Given
        String userId = TEST_USER_ID;
        when(spotifyConfig.getFrontendUrl()).thenReturn(FRONTEND_URL);

        // When
        String result = spotifyOAuthService.buildSuccessRedirectUrl(userId);

        // Then
        assertEquals("http://localhost:9090/auth/callback?user_id=test_user_id", result);
    }

    @Test
    void buildErrorRedirectUrl_ValidErrorMessage_returnsFormattedUrl() {
        // Given
        String errorMessage = "Authentication failed";
        when(spotifyConfig.getFrontendUrl()).thenReturn(FRONTEND_URL);

        // When
        String result = spotifyOAuthService.buildErrorRedirectUrl(errorMessage);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("http://localhost:9090/login?error="));
        assertTrue(result.contains("Authentication"));
    }

    @Test
    void buildErrorRedirectUrl_ErrorMessageWithSpecialCharacters_returnsEncodedUrl() {
        // Given
        String errorMessage = "Error 1234 &$%$%$ 4321";
        when(spotifyConfig.getFrontendUrl()).thenReturn(FRONTEND_URL);

        // When
        String result = spotifyOAuthService.buildErrorRedirectUrl(errorMessage);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("http://localhost:9090/login?error="));
        assertFalse(result.contains("Error 1234 &$%$%$ 4321"));
    }

    @Test
    void exchangeCodeForToken_ValidCode_returnsUserId() throws Exception {
        // Given
        String code = "auth_code_123";
        String generatedUserId = TEST_USER_ID;
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                3600,
                SCOPES);

        when(spotifyConfig.getTokenUrl()).thenReturn(TOKEN_URL);
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);
        when(spotifyConfig.getRedirectUri()).thenReturn(REDIRECT_URI);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);

        try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class);
                MockedStatic<UserIdGenerator> mockedUserIdGenerator = mockStatic(UserIdGenerator.class)) {

            mockedRestClient.when(RestClient::create).thenReturn(restClient);
            mockedUserIdGenerator.when(UserIdGenerator::generateUserId).thenReturn(generatedUserId);

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyTokenResponse.class)).thenReturn(tokenResponse);

            // When
            String result = spotifyOAuthService.exchangeCodeForToken(code);

            // Then
            assertEquals(generatedUserId, result);
            verify(tokenStore).saveToken(generatedUserId, tokenResponse);
        }
    }

    @Test
    void exchangeCodeForToken_NullTokenResponse_throwsRuntimeException() throws Exception {
        // Given
        String code = "auth_code_123";

        when(spotifyConfig.getTokenUrl()).thenReturn(TOKEN_URL);
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);
        when(spotifyConfig.getRedirectUri()).thenReturn(REDIRECT_URI);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);

        try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
            mockedRestClient.when(RestClient::create).thenReturn(restClient);

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyTokenResponse.class)).thenReturn(null);

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                spotifyOAuthService.exchangeCodeForToken(code);
            });

            assertEquals("Failed to retrieve token from Spotify", exception.getMessage());
        }
    }

    @Test
    void exchangeCodeForToken_TooManyRequestsRetrySucceeds_returnsUserId() throws Exception {
        // Given
        String code = "auth_code_123";
        String generatedUserId = TEST_USER_ID;
        SpotifyTokenResponse tokenResponse = new SpotifyTokenResponse(
                "access_token_123",
                TOKEN_TYPE,
                "refresh_token_123",
                3600,
                SCOPES);

        HttpClientErrorException.TooManyRequests tooManyRequestsException = (HttpClientErrorException.TooManyRequests) HttpClientErrorException
                .create(
                        HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", new HttpHeaders(), new byte[0], null);

        when(spotifyConfig.getTokenUrl()).thenReturn(TOKEN_URL);
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);
        when(spotifyConfig.getRedirectUri()).thenReturn(REDIRECT_URI);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);
        when(retryUtils.getRetryAfterSeconds(tooManyRequestsException)).thenReturn(1L);

        try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class);
                MockedStatic<UserIdGenerator> mockedUserIdGenerator = mockStatic(UserIdGenerator.class)) {

            mockedRestClient.when(RestClient::create).thenReturn(restClient);
            mockedUserIdGenerator.when(UserIdGenerator::generateUserId).thenReturn(generatedUserId);

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyTokenResponse.class))
                    .thenThrow(tooManyRequestsException)
                    .thenReturn(tokenResponse);

            // When
            String result = spotifyOAuthService.exchangeCodeForToken(code);

            // Then
            assertEquals(generatedUserId, result);
            verify(tokenStore).saveToken(generatedUserId, tokenResponse);
            verify(retryUtils).getRetryAfterSeconds(tooManyRequestsException);
        }
    }

    @Test
    void exchangeCodeForToken_MaxRetriesExceeded_throwsRuntimeException() throws Exception {
        // Given
        String code = "auth_code_123";
        HttpClientErrorException.TooManyRequests tooManyRequestsException = (HttpClientErrorException.TooManyRequests) HttpClientErrorException
                .create(
                        HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", new HttpHeaders(), new byte[0], null);

        when(spotifyConfig.getTokenUrl()).thenReturn(TOKEN_URL);
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);
        when(spotifyConfig.getRedirectUri()).thenReturn(REDIRECT_URI);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(2);
        when(retryUtils.getRetryAfterSeconds(tooManyRequestsException)).thenReturn(1L);

        try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
            mockedRestClient.when(RestClient::create).thenReturn(restClient);

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyTokenResponse.class)).thenThrow(tooManyRequestsException);

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                spotifyOAuthService.exchangeCodeForToken(code);
            });

            assertTrue(exception.getMessage().contains("Max retry attempts reached"));
            assertTrue(exception.getMessage().contains("2 attempts"));
        }
    }

    @Test
    void refreshToken_ValidUserIdWithRefreshToken_returnsTrue() {
        // Given
        String userId = TEST_USER_ID;
        SpotifyTokenResponse existingToken = new SpotifyTokenResponse(
                "old_access_token",
                TOKEN_TYPE,
                "refresh_token_123",
                3600,
                SCOPES);
        SpotifyTokenResponse newToken = new SpotifyTokenResponse(
                "new_access_token",
                TOKEN_TYPE,
                null, // No new refresh token
                3600,
                SCOPES);

        when(tokenStore.getToken(userId)).thenReturn(existingToken);
        when(spotifyConfig.getTokenUrl()).thenReturn(TOKEN_URL);
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);

        try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
            mockedRestClient.when(RestClient::create).thenReturn(restClient);

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyTokenResponse.class)).thenReturn(newToken);

            // When
            boolean result = spotifyOAuthService.refreshToken(userId);

            // Then
            assertTrue(result);
            verify(tokenStore).saveToken(eq(userId),
                    argThat(token -> "new_access_token".equals(token.getAccessToken()) &&
                            "refresh_token_123".equals(token.getRefreshToken())));
        }
    }

    @Test
    void refreshToken_NoTokenForUser_returnsFalse() {
        // Given
        String userId = TEST_USER_ID;
        when(tokenStore.getToken(userId)).thenReturn(null);

        // When
        boolean result = spotifyOAuthService.refreshToken(userId);

        // Then
        assertFalse(result);
        verify(tokenStore, never()).saveToken(anyString(), any());
    }

    @Test
    void refreshToken_NoRefreshToken_returnsFalse() {
        // Given
        String userId = TEST_USER_ID;
        SpotifyTokenResponse tokenWithoutRefresh = new SpotifyTokenResponse(
                "access_token",
                TOKEN_TYPE,
                null,
                3600,
                SCOPES);
        when(tokenStore.getToken(userId)).thenReturn(tokenWithoutRefresh);

        // When
        boolean result = spotifyOAuthService.refreshToken(userId);

        // Then
        assertFalse(result);
        verify(tokenStore, never()).saveToken(anyString(), any());
    }

    @Test
    void refreshToken_EmptyRefreshToken_returnsFalse() {
        // Given
        String userId = TEST_USER_ID;
        SpotifyTokenResponse tokenWithEmptyRefresh = new SpotifyTokenResponse(
                "access_token",
                TOKEN_TYPE,
                "",
                3600,
                SCOPES);
        when(tokenStore.getToken(userId)).thenReturn(tokenWithEmptyRefresh);

        // When
        boolean result = spotifyOAuthService.refreshToken(userId);

        // Then
        assertFalse(result);
        verify(tokenStore, never()).saveToken(anyString(), any());
    }

    @Test
    void refreshToken_ExceptionDuringRefresh_returnsFalse() {
        // Given
        String userId = TEST_USER_ID;
        SpotifyTokenResponse existingToken = new SpotifyTokenResponse(
                "access_token",
                TOKEN_TYPE,
                "refresh_token_123",
                3600,
                SCOPES);

        when(tokenStore.getToken(userId)).thenReturn(existingToken);
        when(spotifyConfig.getTokenUrl()).thenReturn(TOKEN_URL);
        when(spotifyConfig.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(spotifyConfig.getClientSecret()).thenReturn(TEST_CLIENT_SECRET);
        when(retryConfig.getMaxRetryAttempts()).thenReturn(3);

        try (MockedStatic<RestClient> mockedRestClient = mockStatic(RestClient.class)) {
            mockedRestClient.when(RestClient::create).thenReturn(restClient);

            when(restClient.post()).thenReturn(requestBodyUriSpec);
            when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.body(anyString())).thenReturn(requestBodySpec);
            when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.body(SpotifyTokenResponse.class)).thenThrow(new RuntimeException("Network error"));

            // When
            boolean result = spotifyOAuthService.refreshToken(userId);

            // Then
            assertFalse(result);
            verify(tokenStore, never()).saveToken(anyString(), any());
        }
    }

    @Test
    void logout_ValidUserId_removesToken() {
        // Given
        String userId = TEST_USER_ID;

        // When
        spotifyOAuthService.logout(userId);

        // Then
        verify(tokenStore).removeToken(userId);
    }

    @Test
    void logout_NullUserId_callsRemoveToken() {
        // Given
        String userId = null;

        // When & Then
        assertDoesNotThrow(() -> {
            spotifyOAuthService.logout(userId);
        });
        verify(tokenStore).removeToken(null);
    }
}
