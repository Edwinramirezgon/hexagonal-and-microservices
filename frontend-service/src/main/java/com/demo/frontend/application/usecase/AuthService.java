package com.demo.frontend.application.usecase;

import com.demo.frontend.application.port.in.AuthUseCase;
import com.demo.frontend.application.port.out.AuthServicePort;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {

    private final AuthServicePort authServicePort;

    public AuthService(AuthServicePort authServicePort) {
        this.authServicePort = authServicePort;
    }

    @Override
    public AuthResult login(String username, String password) {
        return authServicePort.login(username, password);
    }

    @Override
    public void register(String username, String email, String password) {
        authServicePort.register(username, email, password);
    }

    @Override
    public void logout(String token) {
        authServicePort.logout(token);
    }

    @Override
    public int countUsers() {
        return authServicePort.countUsers();
    }
}
