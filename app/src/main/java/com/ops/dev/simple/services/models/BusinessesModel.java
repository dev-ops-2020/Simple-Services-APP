package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class BusinessesModel implements Serializable {

    private String id;
    private String name;
    private String description;
    private String slogan;
    private String owner ;
    private String score;
    private String status;
    private String logo;
    private String picture;
    private String pictures;
    private String phones;
    private String schedule;
    private String networks;
    private String categories;
    private String latitude;
    private String longitude;
    private String idMembership;

    public BusinessesModel() {
    }

    public BusinessesModel(String id, String name, String description, String slogan, String owner, String score, String status, String logo, String picture ,String pictures, String phones, String schedule, String networks, String categories, String latitude, String longitude, String idMembership) {
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
        this.phones = phones;
        this.schedule = schedule;
        this.networks = networks;
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

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getNetworks() {
        return networks;
    }

    public void setNetworks(String networks) {
        this.networks = networks;
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
