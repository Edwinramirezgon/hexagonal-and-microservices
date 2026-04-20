package com.demo.reservations.infrastructure.config;

import com.demo.reservations.application.port.out.AuthServicePort;
import com.demo.reservations.application.port.out.EmailQueuePort;
import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.infrastructure.adapter.out.AuthServiceAdapter;
import com.demo.reservations.infrastructure.adapter.out.RedisEmailQueueAdapter;
import com.demo.reservations.infrastructure.adapter.out.SqlServerReservationAdapter;
import com.demo.reservations.infrastructure.adapter.out.persistence.JpaReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PersistenceConfig {

    @Bean
    public ReservationRepository reservationRepository(JpaReservationRepository jpa) {
        return new SqlServerReservationAdapter(jpa);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthServicePort authServicePort(RestTemplate restTemplate,
                                           @Value("${auth.service.url}") String url) {
        return new AuthServiceAdapter(restTemplate, url);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port,
            @Value("${spring.data.redis.password:}") String password) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        if (password != null && !password.isBlank()) config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public EmailQueuePort emailQueuePort(StringRedisTemplate redisTemplate,
                                         ObjectMapper objectMapper,
                                         @Value("${email.queue.key}") String queueKey) {
        return new RedisEmailQueueAdapter(redisTemplate, objectMapper, queueKey);
    }
}
