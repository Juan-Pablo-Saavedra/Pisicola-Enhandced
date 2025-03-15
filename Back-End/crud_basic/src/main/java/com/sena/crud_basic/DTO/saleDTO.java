package com.sena.crud_basic.DTO;

import java.util.Date;
import com.sena.crud_basic.model.customer;

public class saleDTO {
    private int id;
    private Date date;
    private float total;
    private customer customer;

    public saleDTO() {
    }

    public saleDTO(int id, Date date, float total, customer customer) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.customer = customer;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public customer getCustomer() {
        return customer;
    }

    public void setCustomer(customer customer) {
        this.customer = customer;
    }
}
