package com.ef.parser.repository;

import com.ef.parser.domain.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LogFileRepository extends JpaRepository<LogEntry, Long> {

    @Query("select e from LogEntry e where e.date >= ?1 and e.date < ?2 group by e.ip having count(e.ip) >= ?3")
    List<LogEntry> findByDatesAndTreshold(Date startDate, Date endDate, Long treshold);
}
