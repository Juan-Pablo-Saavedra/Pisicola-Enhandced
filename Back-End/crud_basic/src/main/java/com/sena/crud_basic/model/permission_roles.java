package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "permission_role")
public class permission_roles{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_roleid")
    private int permission_roleid;

    @ManyToOne
    @JoinColumn(name = "pageid", nullable = false)
    private pages page;

    @ManyToOne
    @JoinColumn(name = "roleid", nullable = false)
    private roles role;

    @Column(name = "type", nullable = false)
    private String type;

    public permission_roles() {
        // Default constructor
    }

    public permission_roles(int permission_roleid, pages page, roles role, String type) {
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