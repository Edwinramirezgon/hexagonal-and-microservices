package com.demo.reservations.infrastructure.adapter.in;

import com.demo.reservations.application.port.in.ReservationUseCase;
import com.demo.reservations.domain.exception.RoomNotAvailableException;
import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.in.dto.ReservationRequest;
import com.demo.reservations.infrastructure.adapter.in.dto.ReservationResponse;
import com.demo.reservations.infrastructure.adapter.in.mapper.ReservationRequestMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationUseCase useCase;
    private final Key                jwtKey;

    public ReservationController(ReservationUseCase useCase,
                                 @Value("${jwt.secret}") String jwtSecret) {
        this.useCase = useCase;
        this.jwtKey  = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationRequest request,
                                    @RequestHeader("Authorization") String authHeader) {
        try {
            String username  = extractUsername(authHeader);
            request.setCreatedBy(username);
            Reservation domain   = ReservationRequestMapper.toDomain(request);
            Reservation saved    = useCase.create(domain);
            return ResponseEntity.status(HttpStatus.CREATED).body(ReservationRequestMapper.toResponse(saved));
        } catch (RoomNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            String role     = extractRole(authHeader);
            String username = extractUsername(authHeader);
            List<ReservationResponse> response;
            if ("ADMIN".equals(role)) {
                response = useCase.findAll().stream().map(ReservationRequestMapper::toResponse).toList();
            } else {
                response = useCase.findByUser(username).stream().map(ReservationRequestMapper::toResponse).toList();
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ReservationRequestMapper.toResponse(useCase.findById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody ReservationRequest request,
                                    @RequestHeader("Authorization") String authHeader) {
        try {
            String username = extractUsername(authHeader);
            request.setCreatedBy(username);
            Reservation domain  = ReservationRequestMapper.toDomain(request);
            Reservation updated = useCase.update(id, domain);
            return ResponseEntity.ok(ReservationRequestMapper.toResponse(updated));
        } catch (RoomNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            useCase.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    private String extractUsername(String authHeader) {
        return parseClaims(authHeader).getSubject();
    }

    private String extractRole(String authHeader) {
        return (String) parseClaims(authHeader).get("role");
    }

    private Claims parseClaims(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return Jwts.parserBuilder().setSigningKey(jwtKey).build()
                .parseClaimsJws(token).getBody();
    }
}
