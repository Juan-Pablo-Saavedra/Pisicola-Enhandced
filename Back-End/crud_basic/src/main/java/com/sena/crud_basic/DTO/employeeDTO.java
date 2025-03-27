package com.sena.crud_basic.DTO;

public class employeeDTO {
    private String name;
    private String position;
    private String phone;
    private String password;
    private String email;

    public employeeDTO() {
    }

    public employeeDTO(String name, String position, String phone, String password, String email) {

        this.name = name;
        this.position = position;
        this.phone = phone;
        this.password = password;
        this.email = email;
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
