package com.sena.crud_basic.DTO;

import com.sena.crud_basic.model.supplier;

public class foodDTO {
    
    private int id;
    private String type;
    private String brand;
    private supplier supplier;

    public foodDTO() {}

    public foodDTO(int id, String type, String brand, supplier supplier) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(supplier supplier) {
        this.supplier = supplier;
    }
}
