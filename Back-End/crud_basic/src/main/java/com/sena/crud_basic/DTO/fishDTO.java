package com.sena.crud_basic.DTO;

public class fishDTO {
    private int id;
    private String species;
    private String size;
    private float weight;

    public fishDTO() {
    }

    public fishDTO(int id, String species, String size, float weight) {
        this.id = id;
        this.species = species;
        this.size = size;
        this.weight = weight;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
