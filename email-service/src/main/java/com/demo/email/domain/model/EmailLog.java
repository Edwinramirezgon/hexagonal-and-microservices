package com.demo.email.domain.model;

import java.time.LocalDateTime;

public class EmailLog {

    private Long          id;
    private String        to;
    private String        subject;
    private String        status;
    private LocalDateTime sentAt;
    private String        errorMessage;

    private EmailLog() {}

    public static EmailLog success(String to, String subject) {
        EmailLog log    = new EmailLog();
        log.to          = to;
        log.subject     = subject;
        log.status      = "SENT";
        log.sentAt      = LocalDateTime.now();
        return log;
    }

    public static EmailLog failure(String to, String subject, String errorMessage) {
        EmailLog log      = new EmailLog();
        log.to            = to;
        log.subject       = subject;
        log.status        = "FAILED";
        log.sentAt        = LocalDateTime.now();
        log.errorMessage  = errorMessage;
        return log;
    }

    public Long          getId()           { return id; }
    public void          setId(Long id)    { this.id = id; }
    public String        getTo()           { return to; }
    public String        getSubject()      { return subject; }
    public String        getStatus()       { return status; }
    public LocalDateTime getSentAt()       { return sentAt; }
    public String        getErrorMessage() { return errorMessage; }
}
