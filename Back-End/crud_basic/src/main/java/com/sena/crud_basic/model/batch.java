package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "batch")
public class batch {
   /*
     * @ID = es una llave primaria o PK
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fish_id")
    private fish fish;

    @ManyToOne
    @JoinColumn(name = "tank_id")
    private tank tank;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private food food;

    public batch(int id, int quantity, fish fish, tank tank, food food) {
        this.id = id;
        this.quantity = quantity;
        this.fish = fish;
        this.tank = tank;
        this.food = food;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getquantity() {
        return quantity;
    }

    public void setquantity(int quantity) {
        this.quantity = quantity;
    }

    public fish getfish() {
        return fish;
    }

    public void setfish(fish fish) {
        this.fish = fish;
    }

    public tank gettank() {
        return tank;
    }

    public void settank(tank tank) {
        this.tank = tank;
    }

    public food getfood() {
        return food;
    }

    public void setfood(food food) {
        this.food = food;
    }
}
