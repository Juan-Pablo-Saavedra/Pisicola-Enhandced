package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "supplier_employee")
public class supplier_employee {
   /*
     * @ID = es una llave primaria o PK
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private supplier supplier;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private employee employee;

    public supplier_employee(int id, supplier supplier, employee employee) {
        this.id = id;
        this.supplier = supplier;
        this.employee = employee;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public supplier getsupplier() {
        return supplier;
    }

    public void setsupplier(supplier supplier) {
        this.supplier = supplier;
    }

    public employee getemployee() {
        return employee;
    }

    public void setemployee(employee employee) {
        this.employee = employee;
    }
}
