package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "food")
public class food {
   /*
     * @ID = es una llave primaria o PK
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type", length = 255, nullable = false)
    private String type;

    @Column(name = "brand", length = 255, nullable = false)
    private String brand;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private supplier supplier;

    public food(int id, String type, String brand, supplier supplier) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.supplier = supplier;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getbrand() {
        return brand;
    }

    public void setbrand(String brand) {
        this.brand = brand;
    }

    public supplier getsupplier() {
        return supplier;
    }

    public void setsupplier(supplier supplier) {
        this.supplier = supplier;
    }
}
