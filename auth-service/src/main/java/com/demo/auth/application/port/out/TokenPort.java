package com.demo.auth.application.port.out;

public interface TokenPort {
    String generateToken(String username, String role);
    boolean validateToken(String token);
    String extractUsername(String token);
    String extractRole(String token);
    void blacklist(String token);
    boolean isBlacklisted(String token);
}
