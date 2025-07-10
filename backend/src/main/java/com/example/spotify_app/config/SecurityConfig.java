package com.example.spotify_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "/",
                                "/auth/spotify",
                                "/auth/spotify/callback",
                                "/me/**",
                                "/test",
                                "/auth/spotify/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.disable()).build();
    }
}
