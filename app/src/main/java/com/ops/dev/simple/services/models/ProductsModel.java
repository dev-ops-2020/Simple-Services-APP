package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class ProductsModel implements Serializable {

    private String id;
    private String name;
    private String description;
    private String pictures;
    private String prices;
    private String status;
    private String idCategories;
    private String idBusiness;

    public ProductsModel() {
    }

    public ProductsModel(String id, String name, String description, String pictures, String prices, String status, String idCategories, String idBusiness) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pictures = pictures;
        this.prices = prices;
        this.status = status;
        this.idCategories = idCategories;
        this.idBusiness = idBusiness;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdCategories() {
        return idCategories;
    }

    public void setIdCategories(String idCategories) {
        this.idCategories = idCategories;
    }

    public String getIdBusiness() {
        return idBusiness;
    }

    public void setIdBusiness(String idBusiness) {
        this.idBusiness = idBusiness;
    }
}
