package com.sena.crud_basic.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sena.crud_basic.model.rol;

public class employeeDTO {
    private Integer id;
    private String name;
    private rol position;
    private String phone;
    private String password;
    private String email;
    private String token; // 🔐 Token del reCAPTCHA

    // ✅ Constructor vacío requerido por Spring para deserializar
    public employeeDTO() {
    }

    // ✅ Constructor completo con anotaciones para asegurar correcta deserialización
    @JsonCreator
    public employeeDTO(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("position") rol position,
        @JsonProperty("phone") String phone,
        @JsonProperty("password") String password,
        @JsonProperty("email") String email,
        @JsonProperty("token") String token
    ) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.token = token;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public rol getPosition() {
        return position;
    }

    public void setPosition(rol position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
