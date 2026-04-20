package com.demo.email.application.usecase;

import com.demo.email.application.port.out.EmailLogRepository;
import com.demo.email.application.port.out.EmailQueuePort;
import com.demo.email.application.port.out.EmailSenderPort;
import com.demo.email.domain.model.EmailLog;
import com.demo.email.domain.model.EmailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailWorkerService {

    private final EmailQueuePort     emailQueuePort;
    private final EmailSenderPort    emailSenderPort;
    private final EmailLogRepository emailLogRepository;

    public EmailWorkerService(EmailQueuePort emailQueuePort,
                              EmailSenderPort emailSenderPort,
                              EmailLogRepository emailLogRepository) {
        this.emailQueuePort     = emailQueuePort;
        this.emailSenderPort    = emailSenderPort;
        this.emailLogRepository = emailLogRepository;
    }

    @Scheduled(fixedDelay = 3000)
    public void processQueue() {
        EmailMessage message;
        while ((message = emailQueuePort.dequeue()) != null) {
            try {
                emailSenderPort.send(message);
                emailLogRepository.save(EmailLog.success(message.getTo(), message.getSubject()));
            } catch (Exception e) {
                emailLogRepository.save(EmailLog.failure(message.getTo(), message.getSubject(), e.getMessage()));
            }
        }
    }
}
