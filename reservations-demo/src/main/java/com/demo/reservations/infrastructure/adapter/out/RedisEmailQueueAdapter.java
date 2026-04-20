package com.demo.reservations.infrastructure.adapter.out;

import com.demo.reservations.application.port.out.EmailQueuePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

public class RedisEmailQueueAdapter implements EmailQueuePort {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper        objectMapper;
    private final String              queueKey;

    public RedisEmailQueueAdapter(StringRedisTemplate redisTemplate,
                                  ObjectMapper objectMapper,
                                  String queueKey) {
        this.redisTemplate = redisTemplate;
        this.objectMapper  = objectMapper;
        this.queueKey      = queueKey;
    }

    @Override
    public void publish(String to, String subject, String body) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "to", to, "subject", subject, "body", body));
            redisTemplate.opsForList().rightPush(queueKey, json);
        } catch (Exception e) {
            throw new RuntimeException("Error al publicar email en cola.", e);
        }
    }
}
