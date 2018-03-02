package com.ef.parser.repository;

import com.ef.parser.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
