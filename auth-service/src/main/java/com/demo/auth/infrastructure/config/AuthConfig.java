package com.demo.auth.infrastructure.config;

import com.demo.auth.application.port.out.NotificationPort;
import com.demo.auth.application.port.out.TokenPort;
import com.demo.auth.application.port.out.UserRepository;
import com.demo.auth.infrastructure.adapter.out.JwtTokenAdapter;
import com.demo.auth.infrastructure.adapter.out.RedisNotificationAdapter;
import com.demo.auth.infrastructure.adapter.out.SqlServerUserAdapter;
import com.demo.auth.infrastructure.adapter.out.persistence.JpaUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableAsync
public class AuthConfig {

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
        return new ObjectMapper();
    }

    @Bean
    public UserRepository userRepository(JpaUserRepository jpa) {
        return new SqlServerUserAdapter(jpa);
    }

    @Bean
    public TokenPort tokenPort(@Value("${jwt.secret}") String secret,
                               @Value("${jwt.expiration-ms}") long expirationMs,
                               StringRedisTemplate redisTemplate) {
        return new JwtTokenAdapter(secret, expirationMs, redisTemplate);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public NotificationPort notificationPort(StringRedisTemplate redisTemplate,
                                             ObjectMapper objectMapper,
                                             @Value("${email.queue.key}") String queueKey) {
        return new RedisNotificationAdapter(redisTemplate, objectMapper, queueKey);
    }
}
