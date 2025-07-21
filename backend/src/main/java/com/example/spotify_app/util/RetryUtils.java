package com.example.spotify_app.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.example.spotify_app.config.RetryConfig;

@Component
public class RetryUtils {
    private final RetryConfig retryConfig;

    public RetryUtils(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    public long getRetryAfterSeconds(HttpClientErrorException.TooManyRequests e) {
        try {
            var headers = e.getResponseHeaders();
            if (headers != null) {
                String retryAfterHeader = headers.getFirst("Retry-After");
                if (retryAfterHeader != null && !retryAfterHeader.isEmpty()) {
                    long retryAfter = Long.parseLong(retryAfterHeader);
                    return Math.min(retryAfter, retryConfig.getMaxRetryDelaySeconds());
                }
            }
        } catch (NumberFormatException nfe) {
            String headerValue = "null";
            var headers = e.getResponseHeaders();
            if (headers != null) {
                String tempHeader = headers.getFirst("Retry-After");
                headerValue = (tempHeader != null) ? tempHeader : "null";
            }
            System.err.println("Invalid Retry-After header format: " + headerValue);
        }

        return retryConfig.getDefaultRetryDelaySeconds();
    }
}
