package com.sena.crud_basic.DTO;

public class supplierDTO {
    private int id;
    private String name;
    private String contact;
    private String phone;

    public supplierDTO() {
    }

    public supplierDTO(int id, String name, String contact, String phone) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.phone = phone;
    }

    // Getters y Setters

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

    public String getContact() {
       return contact;
    }

    public void setContact(String contact) {
       this.contact = contact;
    }

    public String getPhone() {
       return phone;
    }

    public void setPhone(String phone) {
       this.phone = phone;
    }
}
