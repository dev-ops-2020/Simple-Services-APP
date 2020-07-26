package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class CategoriesModel implements Serializable {

    private String id;
    private String name;
    private int icon;
    private boolean checked;

    public CategoriesModel() {

    }

    public CategoriesModel(String id, String name, int icon) {
        this.id = id;
        this.name = name;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
/*
    public String toString() {
        return name;
    }
    
    public void toggleChecked() {
        checked = !checked;
    }
*/
}