package com.demo.auth.infrastructure.adapter.in;

import com.demo.auth.infrastructure.adapter.out.persistence.JpaUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final JpaUserRepository jpa;

    public UserController(JpaUserRepository jpa) {
        this.jpa = jpa;
    }

    @GetMapping("/email")
    public ResponseEntity<?> getEmailByUsername(@RequestParam String username) {
        return jpa.findByUsername(username)
                .map(u -> ResponseEntity.ok(Map.of("email", u.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        List<Map<String, Object>> users = jpa.findAll().stream()
                .map(u -> Map.<String, Object>of(
                        "id",       u.getId(),
                        "username", u.getUsername(),
                        "email",    u.getEmail(),
                        "role",     u.getRole()
                ))
                .toList();
        return ResponseEntity.ok(users);
    }
}
