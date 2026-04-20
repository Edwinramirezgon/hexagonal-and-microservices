package com.demo.email.domain.model;

public class EmailMessage {

    private String to;
    private String subject;
    private String body;

    private EmailMessage() {}

    public static EmailMessage create(String to, String subject, String body) {
        if (to == null || to.isBlank())
            throw new IllegalArgumentException("El destinatario es obligatorio.");
        if (subject == null || subject.isBlank())
            throw new IllegalArgumentException("El asunto es obligatorio.");
        if (body == null || body.isBlank())
            throw new IllegalArgumentException("El cuerpo del mensaje es obligatorio.");

        EmailMessage m = new EmailMessage();
        m.to      = to;
        m.subject = subject;
        m.body    = body;
        return m;
    }

    public String getTo()      { return to; }
    public String getSubject() { return subject; }
    public String getBody()    { return body; }
}
