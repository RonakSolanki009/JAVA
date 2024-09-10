package com.test.user.event;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.test.user.model.AuditLog;
import com.test.user.repository.AuditLogRepository;

@Service
public class KafkaListenerService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "user-topic", groupId = "my-group")
    public void listenUserEvents(String message) {
        AuditLog log = new AuditLog();
        log.setAction(message.split(":")[0].trim());
        log.setEntity("User");
        log.setEntityId(Long.parseLong(message.split(":")[1].trim()));
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}

