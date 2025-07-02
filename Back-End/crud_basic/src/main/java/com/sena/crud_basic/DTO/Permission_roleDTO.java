package com.sena.crud_basic.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sena.crud_basic.model.pages;
import com.sena.crud_basic.model.roles;

public class Permission_roleDTO {
    @JsonProperty("id")
    private int permission_roleid;
    private pages page;
    private roles role;
    private String type;

    public Permission_roleDTO() {
    }

    public Permission_roleDTO(int permission_roleid, pages page, roles role, String type) {
        this.permission_roleid = permission_roleid;
        this.page = page;
        this.role = role;
        this.type = type;
    }

    public int getPermission_roleid() {
        return permission_roleid;
    }
    public void setPermission_roleid(int permission_roleid) {
        this.permission_roleid = permission_roleid;
    }

    public pages getPage() {
        return page;
    }
    public void setPage(pages page) {
        this.page = page;
    }

    public roles getRole() {
        return role;
    }
    public void setRole(roles role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}