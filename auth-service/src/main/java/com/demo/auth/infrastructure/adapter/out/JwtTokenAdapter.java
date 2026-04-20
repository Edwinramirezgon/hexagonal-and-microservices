package com.demo.auth.infrastructure.adapter.out;

import com.demo.auth.application.port.out.TokenPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

public class JwtTokenAdapter implements TokenPort {

    private final Key                 key;
    private final long                expirationMs;
    private final StringRedisTemplate redisTemplate;
    private static final String       BLACKLIST_PREFIX = "blacklist:";

    public JwtTokenAdapter(String secret, long expirationMs, StringRedisTemplate redisTemplate) {
        this.key           = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs  = expirationMs;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            if (isBlacklisted(token)) return false;
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    @Override
    public void blacklist(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();
            if (ttl > 0)
                redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", Duration.ofMillis(ttl));
        } catch (Exception ignored) {}
    }

    @Override
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    private io.jsonwebtoken.Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}
