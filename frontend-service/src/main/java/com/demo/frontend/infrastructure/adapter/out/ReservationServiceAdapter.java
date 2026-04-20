package com.demo.frontend.infrastructure.adapter.out;

import com.demo.frontend.application.port.in.dto.ReservationDto;
import com.demo.frontend.application.port.out.ReservationServicePort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ReservationServiceAdapter implements ReservationServicePort {

    private final RestTemplate restTemplate;
    private final String       baseUrl;

    public ReservationServiceAdapter(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl      = baseUrl;
    }

    @Override
    public List<ReservationDto> findAll(String token) {
        ResponseEntity<List<ReservationDto>> response = restTemplate.exchange(
                baseUrl + "/reservations",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    @Override
    public ReservationDto create(ReservationDto dto, String token) {
        return restTemplate.postForObject(
                baseUrl + "/reservations",
                new HttpEntity<>(dto, authHeaders(token)),
                ReservationDto.class);
    }

    @Override
    public ReservationDto update(Long id, ReservationDto dto, String token) {
        ResponseEntity<ReservationDto> response = restTemplate.exchange(
                baseUrl + "/reservations/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(dto, authHeaders(token)),
                ReservationDto.class);
        return response.getBody();
    }

    @Override
    public void delete(Long id, String token) {
        restTemplate.exchange(
                baseUrl + "/reservations/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(authHeaders(token)),
                Void.class);
    }

    @Override
    public byte[] downloadIcs(Long id, String token) {
        ResponseEntity<byte[]> response = restTemplate.exchange(
                baseUrl + "/calendar/reservation/" + id + ".ics",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(token)),
                byte[].class);
        return response.getBody();
    }

    @Override
    public byte[] downloadAllIcs() {
        ResponseEntity<byte[]> response = restTemplate.exchange(
                baseUrl + "/calendar/reservations.ics",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders(null)),
                byte[].class);
        return response.getBody();
    }

    private HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.setBearerAuth(token);
        return headers;
    }
}
