package com.sena.crud_basic.DTO;

import java.io.Serializable;

public class tankDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private float capacity;
    private String location;
    private String waterType;

    public tankDTO() {}

    public tankDTO(int id, float capacity, String location, String waterType) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
        this.waterType = waterType;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }
}
