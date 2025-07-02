package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "page")
public class pages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pageid")
    private int pageid;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "url", length = 255, nullable = false)
    private String url;

    public pages() {
        // Default constructor
    }

    public pages(int pageid, String name, String url, String description) {
        this.pageid = pageid;
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public int getPageid() {
        return pageid;
    }
    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}