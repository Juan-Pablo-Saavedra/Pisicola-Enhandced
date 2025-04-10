package com.sena.crud_basic.DTO;

import com.sena.crud_basic.model.rol;

public class employeeDTO {
    private Integer id; // Asegúrate de incluir el campo ID
    private String name;
    private rol position;
    private String phone;
    private String password;
    private String email;

    // Constructor vacío
    public employeeDTO() {
    }

    // Constructor con parámetros
    public employeeDTO(Integer id, String name, rol position, String phone, String password, String email) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.password = password;
        this.email = email;
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
}