package com.ef.parser.repository;

import com.ef.parser.domain.Blocked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedRepository extends JpaRepository<Blocked, Long> {
}
