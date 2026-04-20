package com.demo.reservations.infrastructure.adapter.out;

import com.demo.reservations.application.port.in.AuthProxyUseCase;
import com.demo.reservations.application.port.out.AuthServicePort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AuthServiceAdapter implements AuthServicePort {

    private final RestTemplate restTemplate;
    private final String       baseUrl;

    public AuthServiceAdapter(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl      = baseUrl;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            restTemplate.exchange(baseUrl + "/auth/validate",
                    HttpMethod.GET, new HttpEntity<>(headers), Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            var response = restTemplate.exchange(baseUrl + "/auth/me",
                    HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            return (String) response.getBody().get("username");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String extractRole(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            var response = restTemplate.exchange(baseUrl + "/auth/me",
                    HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            return (String) response.getBody().get("role");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public AuthProxyUseCase.AuthResult login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var response = restTemplate.postForObject(baseUrl + "/auth/login",
                new HttpEntity<>(Map.of("username", username, "password", password), headers),
                Map.class);
        return new AuthProxyUseCase.AuthResult(
                (String) response.get("token"),
                (String) response.get("username"),
                (String) response.get("email"),
                (String) response.get("role"));
    }

    @Override
    public void register(String username, String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForObject(baseUrl + "/auth/register",
                new HttpEntity<>(Map.of("username", username, "email", email, "password", password), headers),
                Map.class);
    }

    @Override
    public void logout(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            restTemplate.exchange(baseUrl + "/auth/logout",
                    HttpMethod.POST, new HttpEntity<>(headers), Void.class);
        } catch (Exception ignored) {}
    }

    @Override
    public String getEmailByUsername(String username) {
        try {
            var response = restTemplate.getForObject(
                    baseUrl + "/users/email?username=" + username, Map.class);
            return response != null ? (String) response.get("email") : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public java.util.List<?> getAllUsers() {
        try {
            return restTemplate.getForObject(baseUrl + "/users", java.util.List.class);
        } catch (Exception e) {
            return java.util.List.of();
        }
    }
}