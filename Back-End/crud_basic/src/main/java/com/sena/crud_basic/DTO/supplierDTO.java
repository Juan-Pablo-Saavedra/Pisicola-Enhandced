package com.sena.crud_basic.DTO;

import java.util.List;
import com.sena.crud_basic.model.food;

public class supplierDTO {
    
    private int id;
    private String name;
    private String category;
    private String contact;
    private String email;
    private List<food> foodAssociations;

    public supplierDTO() {}

    // Constructor con 6 parámetros
    public supplierDTO(int id, String name, String category, String contact, String email, List<food> foodAssociations) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.contact = contact;
        this.email = email;
        this.foodAssociations = foodAssociations;
    }

    // Getters & Setters
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
    
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<food> getFoodAssociations() {
        return foodAssociations;
    }
    public void setFoodAssociations(List<food> foodAssociations) {
        this.foodAssociations = foodAssociations;
    }
}
