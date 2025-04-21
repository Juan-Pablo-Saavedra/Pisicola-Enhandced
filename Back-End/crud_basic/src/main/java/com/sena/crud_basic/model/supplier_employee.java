package com.sena.crud_basic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "supplier_employee")
public class supplier_employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private supplier supplier;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private employee employee;

    // Constructor vacío
    public supplier_employee() {
    }

    // Constructor con argumentos
    public supplier_employee(int id, supplier supplier, employee employee) {
        this.id = id;
        this.supplier = supplier;
        this.employee = employee;
    }

    // Getters y Setters corregidos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(supplier supplier) {
        this.supplier = supplier;
    }

    public employee getEmployee() {
        return employee;
    }

    public void setEmployee(employee employee) {
        this.employee = employee;
    }
}
