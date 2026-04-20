package com.demo.reservations.application.port.out;

import com.demo.reservations.application.port.in.AuthProxyUseCase;

public interface AuthServicePort {
    boolean                    validateToken(String token);
    String                     extractUsername(String token);
    String                     extractRole(String token);
    AuthProxyUseCase.AuthResult login(String username, String password);
    void                       register(String username, String email, String password);
    void                       logout(String token);
    String                     getEmailByUsername(String username);
    java.util.List<?>          getAllUsers();
}
