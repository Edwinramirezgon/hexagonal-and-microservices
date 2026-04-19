package com.demo.auth.application.usecase;

import com.demo.auth.application.port.in.RegisterUseCase;
import com.demo.auth.application.port.out.NotificationPort;
import com.demo.auth.application.port.out.UserRepository;
import com.demo.auth.domain.exception.UserAlreadyExistsException;
import com.demo.auth.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements RegisterUseCase {

    private final UserRepository   userRepository;
    private final PasswordEncoder  passwordEncoder;
    private final NotificationPort notificationPort;

    public RegisterService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           NotificationPort notificationPort) {
        this.userRepository   = userRepository;
        this.passwordEncoder  = passwordEncoder;
        this.notificationPort = notificationPort;
    }

    @Override
    public User register(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username))
            throw new UserAlreadyExistsException(username);

        String hash = passwordEncoder.encode(rawPassword);
        User user   = User.create(username, email, hash);
        User saved  = userRepository.save(user);
        notificationPort.sendWelcomeEmail(saved.getEmail(), saved.getUsername());
        return saved;
    }
}
