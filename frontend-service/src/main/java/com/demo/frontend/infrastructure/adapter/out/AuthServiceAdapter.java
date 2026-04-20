package com.demo.frontend.infrastructure.adapter.out;

import com.demo.frontend.application.port.in.AuthUseCase;
import com.demo.frontend.application.port.out.AuthServicePort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class AuthServiceAdapter implements AuthServicePort {

    private final RestTemplate restTemplate;
    private final String       baseUrl;

    public AuthServiceAdapter(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl      = baseUrl;
    }

    @Override
    public AuthUseCase.AuthResult login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var response = restTemplate.postForObject(
                baseUrl + "/auth/login",
                new HttpEntity<>(Map.of("username", username, "password", password), headers),
                Map.class);
        return new AuthUseCase.AuthResult(
                (String) response.get("token"),
                (String) response.get("username"),
                (String) response.get("email"),
                (String) response.get("role"));
    }

    @Override
    public void register(String username, String email, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForObject(
                baseUrl + "/auth/register",
                new HttpEntity<>(Map.of("username", username, "email", email, "password", password), headers),
                Map.class);
    }

    @Override
    public void logout(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            restTemplate.exchange(
                    baseUrl + "/auth/logout",
                    org.springframework.http.HttpMethod.POST,
                    new HttpEntity<>(headers),
                    Map.class);
        } catch (Exception ignored) {}
    }

    @Override
    public int countUsers() {
        try {
            List<?> response = restTemplate.getForObject(baseUrl + "/users", List.class);
            return response != null ? response.size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
