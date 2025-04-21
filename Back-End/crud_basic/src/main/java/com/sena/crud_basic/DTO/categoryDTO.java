package com.sena.crud_basic.DTO;

public class categoryDTO {
    
    private int id;
    private String name;

    public categoryDTO() {}

    public categoryDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
