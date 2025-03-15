package com.sena.crud_basic.DTO;

public class employeeDTO {
    private int id;
    private String name;
    private String position;
    private String phone;

    public employeeDTO() {
    }

    public employeeDTO(int id, String name, String position, String phone) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
