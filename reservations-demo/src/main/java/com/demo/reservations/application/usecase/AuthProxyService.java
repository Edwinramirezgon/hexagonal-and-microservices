package com.demo.reservations.application.usecase;

import com.demo.reservations.application.port.in.AuthProxyUseCase;
import com.demo.reservations.application.port.out.AuthServicePort;
import org.springframework.stereotype.Service;

@Service
public class AuthProxyService implements AuthProxyUseCase {

    private final AuthServicePort authServicePort;

    public AuthProxyService(AuthServicePort authServicePort) {
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
}
