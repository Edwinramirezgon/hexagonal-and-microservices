package com.demo.email.application.port.out;

import com.demo.email.domain.model.EmailLog;


public interface EmailLogRepository {
    EmailLog save(EmailLog log);
}
