package com.demo.frontend.infrastructure.config;

import com.demo.frontend.application.port.out.AuthServicePort;
import com.demo.frontend.application.port.out.ReservationServicePort;
import com.demo.frontend.infrastructure.adapter.out.AuthServiceAdapter;
import com.demo.frontend.infrastructure.adapter.out.ReservationServiceAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FrontendConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthServicePort authServicePort(RestTemplate restTemplate,
                                           @Value("${reservations.service.url}") String url) {
        return new AuthServiceAdapter(restTemplate, url);
    }

    @Bean
    public ReservationServicePort reservationServicePort(RestTemplate restTemplate,
                                                         @Value("${reservations.service.url}") String url) {
        return new ReservationServiceAdapter(restTemplate, url);
    }

}
