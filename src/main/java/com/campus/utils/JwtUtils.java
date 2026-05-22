package com.campus.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET = "campus-market-secret-key-campus-market";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    public static String createToken(Long userId){
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                            + 7 * 24 * 60 * 60 * 1000)
                )
                .signWith(KEY)
                .compact();
    }
    public static Long parseToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }
}
