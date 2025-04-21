package com.sena.crud_basic.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "supplier")
public class supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "category", length = 100, nullable = false)
    private String category;

    @Column(name = "contact", length = 255, nullable = false)
    private String contact;

    @Column(name = "phone", nullable = false)
    private String phone;
    
    @Column(name = "email", nullable = false)
    private String email;

    // Relación con food
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<food> foodAssociations;

    // Constructor vacío
    public supplier() {}

    // Constructor con 6 parámetros
    public supplier(int id, String name, String category, String contact, String phone, String email) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.contact = contact;
        this.phone = phone;
        this.email = email;
    }

    // Constructor sobrecargado con 4 parámetros (se asume que contact, phone y email son iguales)
    public supplier(int id, String name, String category, String contact) {
        this(id, name, category, contact, contact, contact);
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

    public String getPhone() {
         return phone;
    }

    public void setPhone(String phone) {
         this.phone = phone;
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
