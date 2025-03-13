package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "tank")
public class tank {
   /*
     * @ID = es una llave primaria o PK
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "capacity", nullable = false)
    private float capacity;

    @Column(name = "location", length = 255, nullable = false)
    private String location;

    @Column(name = "water_type", length = 255, nullable = false)
    private String waterType;

    public tank(int id, float capacity, String location, String waterType) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
        this.waterType = waterType;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public float getcapacity() {
        return capacity;
    }

    public void setcapacity(float capacity) {
        this.capacity = capacity;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }

    public String getwaterType() {
        return waterType;
    }

    public void setwaterType(String waterType) {
        this.waterType = waterType;
    }
}
