package com.mall.backend.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final Key KEY = Keys.hmacShaKeyFor("MallBackendSecretKeyForJwtToken1234567890".getBytes());
    private static final long EXPIRATION = 24 * 60 * 60 * 1000L;
    private static final long EXPIRATION_LONG = 7 * 24 * 60 * 60 * 1000L;

    public static String generateToken(Long userId, String username, Integer role, boolean rememberMe) {
        long expiration = rememberMe ? EXPIRATION_LONG : EXPIRATION;
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }
}
