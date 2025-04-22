package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "sale")
public class sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "total", nullable = false)
    private float total;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private customer customer;

    // Constructor por defecto (necesario para JPA)
    public sale() {}

    // Constructor con parámetros
    public sale(int id, Date date, float total, customer customer) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.customer = customer;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public Date getdate() {
        return date;
    }

    public void setdate(Date date) {
        this.date = date;
    }

    public float gettotal() {
        return total;
    }

    public void settotal(float total) {
        this.total = total;
    }

    public customer getcustomer() {
        return customer;
    }

    public void setcustomer(customer customer) {
        this.customer = customer;
    }
}
