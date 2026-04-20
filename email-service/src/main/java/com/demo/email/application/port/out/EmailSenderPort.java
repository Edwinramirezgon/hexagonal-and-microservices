package com.demo.email.application.port.out;

import com.demo.email.domain.model.EmailMessage;

public interface EmailSenderPort {
    void send(EmailMessage message);
}
