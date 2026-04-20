package com.demo.auth.infrastructure.adapter.in;

import com.demo.auth.application.port.in.LoginUseCase;
import com.demo.auth.application.port.in.RegisterUseCase;
import com.demo.auth.application.port.out.TokenPort;
import com.demo.auth.domain.exception.InvalidCredentialsException;
import com.demo.auth.domain.exception.UserAlreadyExistsException;
import com.demo.auth.infrastructure.adapter.in.dto.*;
import com.demo.auth.infrastructure.adapter.in.mapper.AuthMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final LoginUseCase    loginUseCase;
    private final TokenPort       tokenPort;

    public AuthController(RegisterUseCase registerUseCase, LoginUseCase loginUseCase, TokenPort tokenPort) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase    = loginUseCase;
        this.tokenPort       = tokenPort;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            var user     = registerUseCase.register(request.getUsername(), request.getEmail(), request.getPassword());
            var response = AuthMapper.toResponse(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginUseCase.LoginResult result = loginUseCase.login(request.getUsername(), request.getPassword());
            var response = new AuthResponse(result.token(), result.username(), result.email(), result.role());
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            tokenPort.blacklist(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (tokenPort.validateToken(token)) return ResponseEntity.ok().build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        try {
            String token    = authHeader.replace("Bearer ", "");
            String username = tokenPort.extractUsername(token);
            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "role",     tokenPort.extractRole(token)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
