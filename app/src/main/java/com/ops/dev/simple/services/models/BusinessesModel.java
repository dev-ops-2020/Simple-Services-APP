package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class BusinessesModel implements Serializable {

    private String id;
    // Owner info
    private String owner;
    private String email;
    // Business info
    private String type;
    private String logo;
    private String name;
    private String desc;
    private String slogan;
    private String phone;
    private String address;
    private double lat;
    private double lng;
    private double dist;
    private String fb;
    private String ig;
    private String wa;
    private boolean delivery;
    private String schedule;
    private String categories;
    private String picture;
    private String pictures;
    private String membershipId;

    public BusinessesModel() {

    }

    public BusinessesModel(String id, String owner, String email, String type, String logo, String name, String desc, String slogan, String phone, String address, double lat, double lng, double dist, String fb, String ig, String wa, boolean delivery, String schedule, String categories, String picture, String pictures, String membershipId) {
        this.id = id;
        this.owner = owner;
        this.email = email;
        this.type = type;
        this.logo = logo;
        this.name = name;
        this.desc = desc;
        this.slogan = slogan;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.dist = dist;
        this.fb = fb;
        this.ig = ig;
        this.wa = wa;
        this.delivery = delivery;
        this.schedule = schedule;
        this.categories = categories;
        this.picture = picture;
        this.pictures = pictures;
        this.membershipId = membershipId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getIg() {
        return ig;
    }

    public void setIg(String ig) {
        this.ig = ig;
    }

    public String getWa() {
        return wa;
    }

    public void setWa(String wa) {
        this.wa = wa;
    }

    public boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
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

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }
}