package com.rishab99058.linkedIn.user_service.utils;

import com.rishab99058.linkedIn.user_service.entity.UserEntity;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(UserEntity user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getId())
                .claim("email", user.getEmail())
                .claim("name", user.getFirstName()+" "+user.getLastName())
                .claim("role", new ArrayList<>())
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000 * 60 * 10 ))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.getId())
                .claim("email", user.getEmail())
                .claim("name", user.getFirstName()+" "+user.getLastName())
                .claim("roles", new ArrayList<>())
                .issuedAt(new Date(now))
                .expiration(new Date(now + 1000L * 60 * 60 * 24*30*6))
                .signWith(getSecretKey())
                .compact();
    }

    public String getUserIdFromJwtToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return claims.getSubject();

    }

    public String getEmailFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class);
    }


}
