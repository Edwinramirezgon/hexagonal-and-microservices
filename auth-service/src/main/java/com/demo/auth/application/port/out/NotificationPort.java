package com.demo.auth.application.port.out;

public interface NotificationPort {
    void sendWelcomeEmail(String to, String username);
}
