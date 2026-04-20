package com.demo.email.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
public class EmailLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String toAddress;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column
    private String errorMessage;

    public EmailLogEntity() {}

    public Long          getId()           { return id; }
    public String        getToAddress()    { return toAddress; }
    public String        getSubject()      { return subject; }
    public String        getStatus()       { return status; }
    public LocalDateTime getSentAt()       { return sentAt; }
    public String        getErrorMessage() { return errorMessage; }

    public void setId(Long id)                       { this.id = id; }
    public void setToAddress(String toAddress)       { this.toAddress = toAddress; }
    public void setSubject(String subject)           { this.subject = subject; }
    public void setStatus(String status)             { this.status = status; }
    public void setSentAt(LocalDateTime sentAt)      { this.sentAt = sentAt; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
