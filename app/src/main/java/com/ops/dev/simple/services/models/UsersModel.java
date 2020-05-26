package com.ops.dev.simple.services.models;

public class UsersModel {
   private String id;
    private String dui;
    private String name;
    private String lastName;
    private String alias;
    private String phone;
    private String email;
    private String password;
    private String birthDate;
    private String gender;
    private String qrCode;
    private String pictures;
    private String points;
    private String status;
    private String rol;
    private String establishmentsFav;
    private String coupons;
    private String idMembership;

    public UsersModel() {
    }

    public UsersModel(String id, String dui, String name, String lastName, String alias, String phone, String email, String password, String birthDate, String gender, String qrCode, String pictures, String points, String status, String rol, String establishmentsFav, String coupons, String idMembership) {
        this.id = id;
        this.dui = dui;
        this.name = name;
        this.lastName = lastName;
        this.alias = alias;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.qrCode = qrCode;
        this.pictures = pictures;
        this.points = points;
        this.status = status;
        this.rol = rol;
        this.establishmentsFav = establishmentsFav;
        this.coupons = coupons;
        this.idMembership = idMembership;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstablishmentsFav() {
        return establishmentsFav;
    }

    public void setEstablishmentsFav(String establishmentsFav) {
        this.establishmentsFav = establishmentsFav;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public String getIdMembership() {
        return idMembership;
    }

    public void setIdMembership(String idMembership) {
        this.idMembership = idMembership;
    }
}
