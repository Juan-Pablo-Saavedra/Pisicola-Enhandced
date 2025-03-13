package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "fish")
public class fish {
   /*
     * @ID = es una llave primaria o PK
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public fish(){}

    @Column(name = "species", length = 255, nullable = false)
    private String species;

    @Column(name = "size", length = 255, nullable = false)
    private String size;

    @Column(name = "weight", nullable = false)
    private float weight;

    public fish(int id, String species, String size, float weight) {
        this.id = id;
        this.species = species;
        this.size = size;
        this.weight = weight;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getspecies() {
        return species;
    }

    public void setspecies(String species) {
        this.species = species;
    }

    public String getsize() {
        return size;
    }

    public void setsize(String size) {
        this.size = size;
    }

    public float getweight() {
        return weight;
    }

    public void setweight(float weight) {
        this.weight = weight;
    }
}
