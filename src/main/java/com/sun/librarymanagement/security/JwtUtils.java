package com.sun.librarymanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

public class JwtUtils {

    private final Long expirationSeconds;
    private final SecretKey secretKey;

    public JwtUtils(String secretKey, Long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(String sub) {
        if (sub == null || sub.isEmpty()) {
            return null;
        }
        Instant exp = Instant.now();
        return Jwts.builder()
            .subject(sub)
            .issuedAt(new Date(exp.toEpochMilli()))
            .expiration(new Date(exp.toEpochMilli() + expirationSeconds * 1000))
            .signWith(secretKey)
            .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
            Instant now = Instant.now();
            Date exp = claims.getExpiration();
            return exp.after(Date.from(now));

        } catch (JwtException e) {
            return false;
        }
    }

    public String getSub(String jwt) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
            return claims.getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
