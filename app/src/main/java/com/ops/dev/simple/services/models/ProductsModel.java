package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class ProductsModel implements Serializable {

    private String id;
    private String type;
    private String name;
    private String desc;
    private String price;
    private boolean available;
    private String picture;
    private String pictures;
    private String tags;
    private String businessId;

    public ProductsModel() {

    }

    public ProductsModel(String id, String type, String name, String desc, String price, boolean available, String picture, String pictures, String tags, String businessId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.available = available;
        this.picture = picture;
        this.pictures = pictures;
        this.tags = tags;
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}