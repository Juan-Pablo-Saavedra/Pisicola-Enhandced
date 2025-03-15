package com.sena.crud_basic.DTO;

import java.util.Date;
import com.sena.crud_basic.model.tank;
import com.sena.crud_basic.model.employee;

public class maintenanceDTO {
    private int id;
    private Date date;
    private String description;
    private tank tank;
    private employee employee;

    public maintenanceDTO() {
    }

    public maintenanceDTO(int id, Date date, String description, tank tank, employee employee) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.tank = tank;
        this.employee = employee;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public tank getTank() {
        return tank;
    }

    public void setTank(tank tank) {
        this.tank = tank;
    }

    public employee getEmployee() {
        return employee;
    }

    public void setEmployee(employee employee) {
        this.employee = employee;
    }
}
