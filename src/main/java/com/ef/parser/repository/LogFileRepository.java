package com.ef.parser.repository;

import com.ef.parser.domain.LogFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogFileRepository extends JpaRepository<LogFile, Long> {
}
