package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity(name = "maintenance")
public class maintenance {
   /*
     * @ID = es una llave primaria o PK
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "tank_id")
    private tank tank;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private employee employee;


    public maintenance (){
    }
    
    public maintenance(int id, Date date, String description, tank tank, employee employee) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.tank = tank;
        this.employee = employee;
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

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public tank gettank() {
        return tank;
    }

    public void settank(tank tank) {
        this.tank = tank;
    }

    public employee getemployee() {
        return employee;
    }

    public void setemployee(employee employee) {
        this.employee = employee;
    }
}
