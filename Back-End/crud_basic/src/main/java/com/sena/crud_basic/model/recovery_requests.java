package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "recovery_request")
public class recovery_requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recovery_requestid")
    private int recovery_requestid;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "token", length = 255, nullable = false)
    private String token;

    @Column(name = "expiration_time", nullable = false)
    private long expirationTime;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private employee id;

    public recovery_requests() {
        // Default constructor
    }

    public recovery_requests(int recovery_requestid, String email, String token, long expirationTime, employee id) {
        this.recovery_requestid = recovery_requestid;
        this.email = email;
        this.token = token;
        this.expirationTime = expirationTime;
        this.id = id;
    }

    public int getRecovery_requestid() {
        return recovery_requestid;
    }
    public void setRecovery_requestid(int recovery_requestid) {
        this.recovery_requestid = recovery_requestid;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public employee getUser() {
        return id;
    }
    public void setUser(employee id) {
        this.id = id;
    }
    
}