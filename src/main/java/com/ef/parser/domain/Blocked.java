package com.ef.parser.domain;


import javax.persistence.*;

@Entity
@Table(name = "BLOCKED")
public class Blocked {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String ip;
    private String reason;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
