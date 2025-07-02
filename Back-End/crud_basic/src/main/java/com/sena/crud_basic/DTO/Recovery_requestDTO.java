package com.sena.crud_basic.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sena.crud_basic.model.employee;

public class Recovery_requestDTO {
    @JsonProperty("id")
     private int recovery_requestid;
    private String email;
    private String token;
    private long expirationTime;
    private employee employee;

    public Recovery_requestDTO() {
    }

    public Recovery_requestDTO(int recovery_requestid, String email, String token, long expirationTime, employee employee) {
        this.recovery_requestid = recovery_requestid;
        this.email = email;
        this.token = token;
        this.expirationTime = expirationTime;
        this.employee = employee;
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

    public employee getEmployee() {
        return employee;
    }
    public void setEmployee(employee employee) {
        this.employee = employee;
    }
    
}