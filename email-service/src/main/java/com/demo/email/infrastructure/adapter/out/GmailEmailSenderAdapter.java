package com.demo.email.infrastructure.adapter.out;

import com.demo.email.application.port.out.EmailSenderPort;
import com.demo.email.domain.exception.EmailDeliveryException;
import com.demo.email.domain.model.EmailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class GmailEmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    public GmailEmailSenderAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(EmailMessage message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(message.getTo());
            mail.setSubject(message.getSubject());
            mail.setText(message.getBody());
            mailSender.send(mail);
        } catch (Exception e) {
            throw new EmailDeliveryException(message.getTo(), e.getMessage());
        }
    }
}
