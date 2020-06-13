package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class PhonesModel implements Serializable {

    private String name;
    private String number;

    public PhonesModel() {
        this.name = "";
        this.number = "";
    }

    public PhonesModel(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}