package com.demo.email.infrastructure.adapter.out;

import com.demo.email.application.port.out.EmailLogRepository;
import com.demo.email.domain.model.EmailLog;
import com.demo.email.infrastructure.adapter.out.persistence.EmailLogEntity;
import com.demo.email.infrastructure.adapter.out.persistence.JpaEmailLogRepository;

public class SqlServerEmailLogAdapter implements EmailLogRepository {

    private final JpaEmailLogRepository jpa;

    public SqlServerEmailLogAdapter(JpaEmailLogRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public EmailLog save(EmailLog log) {
        EmailLogEntity entity = toEntity(log);
        EmailLogEntity saved  = jpa.save(entity);
        return toDomain(saved);
    }

    private EmailLogEntity toEntity(EmailLog log) {
        EmailLogEntity e = new EmailLogEntity();
        e.setId(log.getId());
        e.setToAddress(log.getTo());
        e.setSubject(log.getSubject());
        e.setStatus(log.getStatus());
        e.setSentAt(log.getSentAt());
        e.setErrorMessage(log.getErrorMessage());
        return e;
    }

    private EmailLog toDomain(EmailLogEntity e) {
        if ("SENT".equals(e.getStatus())) {
            EmailLog log = EmailLog.success(e.getToAddress(), e.getSubject());
            log.setId(e.getId());
            return log;
        }
        EmailLog log = EmailLog.failure(e.getToAddress(), e.getSubject(), e.getErrorMessage());
        log.setId(e.getId());
        return log;
    }
}
