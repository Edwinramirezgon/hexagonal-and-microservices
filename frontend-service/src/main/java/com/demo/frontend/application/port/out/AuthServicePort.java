package com.demo.frontend.application.port.out;

import com.demo.frontend.application.port.in.AuthUseCase;

public interface AuthServicePort {
    AuthUseCase.AuthResult login(String username, String password);
    void register(String username, String email, String password);
    void logout(String token);
    int countUsers();
}
