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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fish_id", nullable = false)
    private fish fish;
    
    @ManyToOne
    @JoinColumn(name = "tank_id", nullable = false)
    private tank tank;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private food food;

    public batch() {}

    public batch(int id, int quantity, fish fish, tank tank, food food) {
        this.id = id;
        this.quantity = quantity;
        this.fish = fish;
        this.tank = tank;
        this.food = food;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public fish getFish() {
        return fish;
    }

    public void setFish(fish fish) {
        this.fish = fish;
    }

    public tank getTank() {
        return tank;
    }

    public void setTank(tank tank) {
        this.tank = tank;
    }

    public food getFood() {
        return food;
    }

    public void setFood(food food) {
        this.food = food;
    }
}
