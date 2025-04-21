package com.sena.crud_basic.DTO;

import java.io.Serializable;

public class batchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int quantity;
    private int fishId;
    private int tankId;
    private int foodId;

    // Constructor vacío
    public batchDTO() {
    }

    // Constructor con argumentos
    public batchDTO(int id, int quantity, int fishId, int tankId, int foodId) {
        this.id = id;
        this.quantity = quantity;
        this.fishId = fishId;
        this.tankId = tankId;
        this.foodId = foodId;
    }

    // Getters y Setters
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

    public int getFishId() {
        return fishId;
    }

    public void setFishId(int fishId) {
        this.fishId = fishId;
    }

    public int getTankId() {
        return tankId;
    }

    public void setTankId(int tankId) {
        this.tankId = tankId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}
