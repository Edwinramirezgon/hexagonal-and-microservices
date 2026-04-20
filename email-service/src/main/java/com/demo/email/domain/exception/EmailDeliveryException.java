package com.demo.email.domain.exception;

public class EmailDeliveryException extends RuntimeException {
    public EmailDeliveryException(String to, String cause) {
        super(String.format("No se pudo enviar el email a '%s': %s", to, cause));
    }
}
