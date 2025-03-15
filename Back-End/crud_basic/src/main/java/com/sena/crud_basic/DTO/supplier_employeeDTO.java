package com.sena.crud_basic.DTO;

import com.sena.crud_basic.model.supplier;
import com.sena.crud_basic.model.employee;

public class supplier_employeeDTO {
    private int id;
    private supplier supplier;
    private employee employee;

    public supplier_employeeDTO() {
    }

    public supplier_employeeDTO(int id, supplier supplier, employee employee) {
        this.id = id;
        this.supplier = supplier;
        this.employee = employee;
    }

    // Getters y Setters

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
