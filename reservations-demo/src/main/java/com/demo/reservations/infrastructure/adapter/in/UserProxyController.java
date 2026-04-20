package com.demo.reservations.infrastructure.adapter.in;

import com.demo.reservations.application.port.out.AuthServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserProxyController {

    private final AuthServicePort authServicePort;

    public UserProxyController(AuthServicePort authServicePort) {
        this.authServicePort = authServicePort;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = authHeader != null ? authHeader.replace("Bearer ", "") : null;
            String role  = authServicePort.extractRole(token);
            if (!"ADMIN".equals(role))
                return ResponseEntity.status(403).build();
            return ResponseEntity.ok(authServicePort.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }
}
