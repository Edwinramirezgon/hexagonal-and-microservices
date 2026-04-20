package com.demo.email.infrastructure.adapter.out;

import com.demo.email.application.port.out.EmailQueuePort;
import com.demo.email.domain.model.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

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
    public EmailMessage dequeue() {
        try {
            String json = redisTemplate.opsForList().leftPop(queueKey);
            if (json == null) return null;
            EmailMessageDto dto = objectMapper.readValue(json, EmailMessageDto.class);
            return EmailMessage.create(dto.to(), dto.subject(), dto.body());
        } catch (Exception e) {
            throw new RuntimeException("Error al desencolar el email.", e);
        }
    }

    record EmailMessageDto(String to, String subject, String body) {}
}
