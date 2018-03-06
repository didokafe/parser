package com.ef.parser.repository;

import com.ef.parser.domain.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LogFileRepository extends JpaRepository<LogEntry, Long> {

    //select r.* from log_file r where r.date BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00' GROUP BY r.ip HAVING count(r.ip) > 200;
    @Query("select e from LogEntry  e where e.date between ?1 and ?2 group by e.ip having count(e.ip) > ?3")
    List<LogEntry> findByDatesAndTreshold(Date startDate, Date endDate, Integer treshold);
}
