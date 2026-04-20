package com.demo.auth.infrastructure.adapter.out;

import com.demo.auth.application.port.out.NotificationPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public class RedisNotificationAdapter implements NotificationPort {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper        objectMapper;
    private final String              queueKey;

    public RedisNotificationAdapter(StringRedisTemplate redisTemplate,
                                    ObjectMapper objectMapper,
                                    String queueKey) {
        this.redisTemplate = redisTemplate;
        this.objectMapper  = objectMapper;
        this.queueKey      = queueKey;
    }

    @Async
    @Override
    public void sendWelcomeEmail(String to, String username) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "to",      to,
                    "subject", "Bienvenido al Sistema de Reservas",
                    "body",    "Hola " + username + ", tu cuenta ha sido creada exitosamente. Bienvenido!"));
            redisTemplate.opsForList().rightPush(queueKey, json);
        } catch (Exception ignored) {}
    }
}
