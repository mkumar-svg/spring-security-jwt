package com.security_jwt.config;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Must be at least 256-bit (32 chars) for HS256
    private static final String SECRET_KEY =
            "MY_SUPER_SECRET_KEY_123456789012";

    private static final long EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(UserDetails user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(
                        "roles",
                        user.getAuthorities()
                                .stream()
                                .map(auth -> auth.getAuthority())
                                .collect(Collectors.toList())
                )
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
}

