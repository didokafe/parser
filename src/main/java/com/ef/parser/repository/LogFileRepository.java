package com.ef.parser.repository;

import com.ef.parser.domain.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogFileRepository extends JpaRepository<LogEntry, Long> {
}
