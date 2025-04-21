package com.sena.crud_basic.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "food")
public class food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "brand", nullable = false)
    private String brand;

    @ManyToOne(fetch = FetchType.EAGER)  // Cambiado a EAGER para evitar LazyInitializationException
    @JoinColumn(name = "supplier_id")    // Eliminado nullable=false para mayor flexibilidad
    @JsonBackReference                   // Para evitar ciclos infinitos en la serialización
    private supplier supplier;

    public food() {}

    public food(int id, String type, String brand, supplier supplier) {
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