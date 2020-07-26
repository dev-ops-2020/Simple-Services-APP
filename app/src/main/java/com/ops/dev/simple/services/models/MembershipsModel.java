package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class MembershipsModel implements Serializable {

    private String id;
    private String name;
    private String desc;
    private String price;
    private String priceOff;
    private int priority;
    private int products;
    private int pictures;
    private int entries;
    private boolean checked;

    public MembershipsModel() {

    }

    MembershipsModel(String id, String name, String desc, String price, String priceOff, int priority, int products, int pictures, int entries,boolean checked) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.priceOff = priceOff;
        this.priority = priority;
        this.products = products;
        this.pictures = pictures;
        this.entries = entries;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(String priceOff) {
        this.priceOff = priceOff;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public int getPictures() {
        return pictures;
    }

    public void setPictures(int pictures) {
        this.pictures = pictures;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return name;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}