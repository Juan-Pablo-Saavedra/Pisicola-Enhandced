package com.sena.crud_basic.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "employee")
public class employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", length = 100, nullable = false)
    private rol position;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    // Relación con supplier_employee (un empleado puede estar vinculado a varios proveedores)
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<supplier_employee> supplierAssociations;

    // Constructor vacío
    public employee() {
    }

    // Constructor con argumentos
    public employee(int id, String name, rol position, String phone, String password, String email) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.password = password;
        this.email = email;
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

    public List<supplier_employee> getSupplierAssociations() {
        return supplierAssociations;
    }

    public void setSupplierAssociations(List<supplier_employee> supplierAssociations) {
        this.supplierAssociations = supplierAssociations;
    }

    public static Object findByNameContainingIgnoreCase(String name2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNameContainingIgnoreCase'");
    }
}
