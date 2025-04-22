package com.sena.crud_basic.DTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object para la entidad sale.
 */
public class saleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Date date;
    private float total;
    private int customerId;
    private String customerName;

    public saleDTO() {}

    public saleDTO(int id, Date date, float total, int customerId, String customerName) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.customerId = customerId;
        this.customerName = customerName;
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
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
