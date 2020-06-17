package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class MembershipsModel implements Serializable {

    private String id;
    private String name;
    private String description;
    private String color;
    private String priority;
    private String extras;
    private String status;
    private double price;
    private double priceOff;
    private double priceExtraCoupon;
    private String quantityCoupons;
    private String quantityPromotions;

    public MembershipsModel() {

    }

    public MembershipsModel(String id, String name, String description, String color, String priority, String extras, String status, double price, double priceOff, double priceExtraCoupon, String quantityCoupons, String quantityPromotions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.priority = priority;
        this.extras = extras;
        this.status = status;
        this.price = price;
        this.priceOff = priceOff;
        this.priceExtraCoupon = priceExtraCoupon;
        this.quantityCoupons = quantityCoupons;
        this.quantityPromotions = quantityPromotions;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(double priceOff) {
        this.priceOff = priceOff;
    }

    public double getPriceExtraCoupon() {
        return priceExtraCoupon;
    }

    public void setPriceExtraCoupon(double priceExtraCoupon) {
        this.priceExtraCoupon = priceExtraCoupon;
    }

    public String getQuantityCoupons() {
        return quantityCoupons;
    }

    public void setQuantityCoupons(String quantityCoupons) {
        this.quantityCoupons = quantityCoupons;
    }

    public String getQuantityPromotions() {
        return quantityPromotions;
    }

    public void setQuantityPromotions(String quantityPromotions) {
        this.quantityPromotions = quantityPromotions;
    }
}
