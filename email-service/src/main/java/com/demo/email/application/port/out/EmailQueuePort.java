package com.demo.email.application.port.out;

import com.demo.email.domain.model.EmailMessage;

public interface EmailQueuePort {
    EmailMessage dequeue();
}
