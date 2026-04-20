package com.demo.reservations.application.port.out;

public interface EmailQueuePort {
    void publish(String to, String subject, String body);
}
