package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class TagsModel implements Serializable {

    private String name;

    public TagsModel() {

    }

    public TagsModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}