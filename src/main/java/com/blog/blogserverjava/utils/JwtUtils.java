package com.blog.blogserverjava.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    @Value("${app.jwt-secret}")
    private String secret;
    @Value("${app.jwt-expire}")
    private long expireSeconds;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String issueJwt(JwtPayload payload) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(Map.of(
                        "id", payload.id(),
                        "username", payload.username(),
                        "name", payload.name()
                ))
                .issuedAt(new Date(now))
                .expiration(new Date(now + expireSeconds * 1000))
                .signWith(getKey())
                .compact();
    }

    public JwtPayload verifyJwt(String token) {
        try {
            Map<String, Object> map = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return new JwtPayload((Integer) map.get("id"),(String) map.get("username"),(String) map.get("name"));
        } catch (Exception e) {
            return  null;
        }
    }

}
