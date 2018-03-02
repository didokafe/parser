package com.ef.parser.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOG")
public class Log {

    @Id
    private Long id;
}
