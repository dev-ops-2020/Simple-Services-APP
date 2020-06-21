package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class CategoriesIconModel implements Serializable {

    private String id;
    private String name;
    private int icon;
    private boolean status;

    public CategoriesIconModel() {

    }

    public CategoriesIconModel(String id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public CategoriesIconModel(String id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}