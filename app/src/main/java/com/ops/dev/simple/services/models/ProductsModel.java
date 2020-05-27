package com.ops.dev.simple.services.models;

public class ProductsModel {
   private String id;
    private String name;
    private String description;
    private String quantity;
    private String prices;
    private String pictures;
    private String status;
    private String idCategories;
    private String idEstablishment;

    public ProductsModel() {
    }

    public ProductsModel(String id, String name, String description, String quantity, String prices, String pictures, String status, String idCategories, String idEstablishment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.prices = prices;
        this.pictures = pictures;
        this.status = status;
        this.idCategories = idCategories;
        this.idEstablishment = idEstablishment;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
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

    public String getIdEstablishment() {
        return idEstablishment;
    }

    public void setIdEstablishment(String idEstablishment) {
        this.idEstablishment = idEstablishment;
    }
}