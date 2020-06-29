package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class BusinessesModel implements Serializable {

    private String id;
    private String name;
    private String description;
    private String slogan;
    private String owner;
    private String score;
    private String status;
    private String logo;
    private String picture;
    private String pictures;
    private String phone;
    private String fb;
    private String ig;
    private String wa;
    private String schedule;
    private String categories;
    private String latitude;
    private String longitude;
    private String idMembership;

    public BusinessesModel() {

    }

    public BusinessesModel(String id, String name, String description, String slogan, String owner, String score, String status, String logo, String picture, String pictures, String phone, String fb, String ig, String wa, String schedule, String categories, String latitude, String longitude, String idMembership) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slogan = slogan;
        this.owner = owner;
        this.score = score;
        this.status = status;
        this.logo = logo;
        this.picture = picture;
        this.pictures = pictures;
        this.phone = phone;
        this.fb = fb;
        this.ig = ig;
        this.wa = wa;
        this.schedule = schedule;
        this.categories = categories;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idMembership = idMembership;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIdMembership() {
        return idMembership;
    }

    public void setIdMembership(String idMembership) {
        this.idMembership = idMembership;
    }
}