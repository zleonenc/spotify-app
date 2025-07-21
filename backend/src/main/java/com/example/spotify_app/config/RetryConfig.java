package com.example.spotify_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class RetryConfig {

    @Value("${spotify.retry.max-attempts:3}")
    private int maxRetryAttempts;

    @Value("${spotify.retry.default-delay-seconds:1}")
    private long defaultRetryDelaySeconds;

    @Value("${spotify.retry.max-delay-seconds:300}")
    private long maxRetryDelaySeconds;
}
