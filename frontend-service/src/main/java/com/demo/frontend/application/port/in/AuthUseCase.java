package com.demo.frontend.application.port.in;

public interface AuthUseCase {
    AuthResult login(String username, String password);
    void register(String username, String email, String password);
    void logout(String token);
    int countUsers();

    record AuthResult(String token, String username, String email, String role) {}
}
