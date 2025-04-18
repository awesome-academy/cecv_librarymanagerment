package com.sun.librarymanagement.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtilsConfiguration {

    @Bean
    public JwtUtils getJwtUtils(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.expiration-time}") Long expirationSeconds
    ) {
        return new JwtUtils(secretKey, expirationSeconds);
    }
}
