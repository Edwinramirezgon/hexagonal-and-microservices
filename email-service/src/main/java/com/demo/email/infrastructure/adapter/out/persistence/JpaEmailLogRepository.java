package com.demo.email.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmailLogRepository extends JpaRepository<EmailLogEntity, Long> {}
