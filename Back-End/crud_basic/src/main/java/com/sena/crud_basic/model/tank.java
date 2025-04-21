package com.sena.crud_basic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tank")
public class tank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "capacity", nullable = false)
    private float capacity;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "water_type", nullable = false)
    private String waterType;

    public tank() {}

    public tank(int id, float capacity, String location, String waterType) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
        this.waterType = waterType;
    }

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
