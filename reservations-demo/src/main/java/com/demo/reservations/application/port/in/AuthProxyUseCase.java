package com.demo.reservations.application.port.in;

public interface AuthProxyUseCase {
    AuthResult login(String username, String password);
    void register(String username, String email, String password);
    void logout(String token);

    record AuthResult(String token, String username, String email, String role) {}
}
