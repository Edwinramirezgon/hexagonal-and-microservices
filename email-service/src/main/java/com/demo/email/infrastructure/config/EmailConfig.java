package com.demo.email.infrastructure.config;

import com.demo.email.application.port.out.EmailLogRepository;
import com.demo.email.application.port.out.EmailQueuePort;
import com.demo.email.application.port.out.EmailSenderPort;
import com.demo.email.infrastructure.adapter.out.GmailEmailSenderAdapter;
import com.demo.email.infrastructure.adapter.out.RedisEmailQueueAdapter;
import com.demo.email.infrastructure.adapter.out.SqlServerEmailLogAdapter;
import com.demo.email.infrastructure.adapter.out.persistence.JpaEmailLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class EmailConfig {

    @Bean
    public EmailQueuePort emailQueuePort(StringRedisTemplate redisTemplate,
                                        ObjectMapper objectMapper,
                                        @Value("${email.queue.key}") String queueKey) {
        return new RedisEmailQueueAdapter(redisTemplate, objectMapper, queueKey);
    }

    @Bean
    public EmailSenderPort emailSenderPort(JavaMailSender mailSender) {
        return new GmailEmailSenderAdapter(mailSender);
    }

    @Bean
    public EmailLogRepository emailLogRepository(JpaEmailLogRepository jpa) {
        return new SqlServerEmailLogAdapter(jpa);
    }
}
