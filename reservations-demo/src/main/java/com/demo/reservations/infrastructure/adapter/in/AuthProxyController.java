package com.demo.reservations.infrastructure.adapter.in;

import com.demo.reservations.application.port.in.AuthProxyUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthProxyController {

    private final AuthProxyUseCase authProxyUseCase;

    public AuthProxyController(AuthProxyUseCase authProxyUseCase) {
        this.authProxyUseCase = authProxyUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            AuthProxyUseCase.AuthResult result =
                    authProxyUseCase.login(body.get("username"), body.get("password"));
            return ResponseEntity.ok(Map.of(
                    "token",    result.token(),
                    "username", result.username(),
                    "email",    result.email(),
                    "role",     result.role()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales invalidas."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            authProxyUseCase.logout(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            authProxyUseCase.register(body.get("username"), body.get("email"), body.get("password"));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuario registrado correctamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
