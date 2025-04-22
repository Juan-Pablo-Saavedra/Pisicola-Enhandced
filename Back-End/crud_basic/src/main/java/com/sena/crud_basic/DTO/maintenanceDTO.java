package com.sena.crud_basic.DTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object para la entidad maintenance.
 */
public class maintenanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Date date;
    private String description;
    private int tankId;
    private String tankName;
    private int employeeId;
    private String employeeName;

    public maintenanceDTO() {}

    public maintenanceDTO(int id, Date date, String description, int tankId, String tankName, int employeeId, String employeeName) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.tankId = tankId;
        this.tankName = tankName;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    // Getters y setters
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
    public int getTankId() {
        return tankId;
    }
    public void setTankId(int tankId) {
        this.tankId = tankId;
    }
    public String getTankName() {
        return tankName;
    }
    public void setTankName(String tankName) {
        this.tankName = tankName;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
