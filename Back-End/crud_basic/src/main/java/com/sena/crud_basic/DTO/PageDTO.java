package com.sena.crud_basic.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageDTO {
    @JsonProperty("id")
    private int pageid;
    private String name;
    private String url;
    private String description;

    public PageDTO() {
    }

    public PageDTO(int pageid, String name, String url, String description) {
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