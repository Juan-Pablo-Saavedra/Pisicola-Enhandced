package com.sena.crud_basic.DTO;

import com.sena.crud_basic.model.fish;
import com.sena.crud_basic.model.tank;
import com.sena.crud_basic.model.food;

public class batchDTO {
    private int id;
    private int quantity;
    private fish fish;
    private tank tank;
    private food food;

    public batchDTO() {
    }

    public batchDTO(int id, int quantity, fish fish, tank tank, food food) {
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
