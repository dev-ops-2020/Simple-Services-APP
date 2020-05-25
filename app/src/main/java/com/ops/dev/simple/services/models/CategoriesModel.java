package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class CategoriesModel implements Serializable {

    private String id;
    private String name;
    private String description;
    private int icon;

    public CategoriesModel() {
        this.id = "";
        this.name = "";
        this.description = "";
        this.icon = 0;
    }

    public CategoriesModel(String id, String name, String description, int icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}