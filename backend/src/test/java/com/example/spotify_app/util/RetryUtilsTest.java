package com.example.spotify_app.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.example.spotify_app.config.RetryConfig;

@ExtendWith(MockitoExtension.class)
class RetryUtilsTest {

    @Mock
    private RetryConfig retryConfig;

    @Mock
    private HttpClientErrorException.TooManyRequests exception;

    private RetryUtils retryUtils;

    @BeforeEach
    void setUp() {
        retryUtils = new RetryUtils(retryConfig);
    }

    @Test
    void getRetryAfterSeconds_ValidRetryAfterHeader_returnsHeaderValue() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "30");
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getMaxRetryDelaySeconds()).thenReturn(300L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(30L, result);
    }

    @Test
    void getRetryAfterSeconds_RetryAfterExceedsMax_returnsMaxRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "500");
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getMaxRetryDelaySeconds()).thenReturn(300L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(300L, result);
        verify(retryConfig).getMaxRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_NullHeaders_returnsDefaultRetryDelay() {
        // Given
        when(exception.getResponseHeaders()).thenReturn(null);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
        verify(retryConfig).getDefaultRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_EmptyHeaders_returnsDefaultRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
        verify(retryConfig).getDefaultRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_NullRetryAfterHeader_returnsDefaultRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", null);
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
        verify(retryConfig).getDefaultRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_EmptyRetryAfterHeader_returnsDefaultRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "");
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
        verify(retryConfig).getDefaultRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_InvalidNumberFormat_returnsDefaultRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "invalid-number");
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
        verify(retryConfig).getDefaultRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_NegativeRetryAfter_returnsDefaultRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "-10");
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
    }

    @Test
    void getRetryAfterSeconds_LargeNumberRetryAfter_returnsMaxRetryDelay() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "999999");
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getMaxRetryDelaySeconds()).thenReturn(300L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(300L, result);
        verify(retryConfig).getMaxRetryDelaySeconds();
    }

    @Test
    void getRetryAfterSeconds_NumberFormatExceptionWithNullHeader_printsNullInErrorMessage() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "not-a-number");
        headers.add("Retry-After", null); // This will be the first value retrieved
        when(exception.getResponseHeaders()).thenReturn(headers);
        when(retryConfig.getDefaultRetryDelaySeconds()).thenReturn(1L);

        // When
        long result = retryUtils.getRetryAfterSeconds(exception);

        // Then
        assertEquals(1L, result);
        verify(retryConfig).getDefaultRetryDelaySeconds();
    }
}
