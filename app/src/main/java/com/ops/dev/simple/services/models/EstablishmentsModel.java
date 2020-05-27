package com.ops.dev.simple.services.models;

public class EstablishmentsModel {
   private String id;
    private String name;
    private String nameCommercial;
    private String slogan;
    private String schedule ;
    private String phones;
    private String score;
    private String pictures;
    private String logo;
    private String owner;
    private String latitude;
    private String longitude;
    private String idMembership;
    private String categories;

    public EstablishmentsModel() {
    }

    public EstablishmentsModel(String id, String name, String nameCommercial, String slogan, String schedule, String phones, String score, String pictures, String logo, String owner, String latitude, String longitude, String idMembership, String categories) {
        this.id = id;
        this.name = name;
        this.nameCommercial = nameCommercial;
        this.slogan = slogan;
        this.schedule = schedule;
        this.phones = phones;
        this.score = score;
        this.pictures = pictures;
        this.logo = logo;
        this.owner = owner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idMembership = idMembership;
        this.categories = categories;
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

    public String getNameCommercial() {
        return nameCommercial;
    }

    public void setNameCommercial(String nameCommercial) {
        this.nameCommercial = nameCommercial;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
