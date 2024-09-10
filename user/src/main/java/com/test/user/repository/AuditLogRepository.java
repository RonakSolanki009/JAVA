package com.test.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.user.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
