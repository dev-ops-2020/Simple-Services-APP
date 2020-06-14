package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class UsersModel  implements Serializable {

    private String id;
    private String name;
    private String alias;
    private String phone;
    private String email;
    private String password;
    private String picture;
    private String qrCode;
    private String points;
    private String status;
    private String rol;
    private String businessFav;
    private String coupons;

    public UsersModel() {
    }

    public UsersModel(String id, String name, String alias, String phone, String email, String password, String qrCode, String picture, String points, String status, String rol, String businessFav, String coupons) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.qrCode = qrCode;
        this.picture = picture;
        this.points = points;
        this.status = status;
        this.rol = rol;
        this.businessFav = businessFav;
        this.coupons = coupons;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String pictures) {
        this.picture = pictures;
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

    public String getBusinessFav() {
        return businessFav;
    }

    public void setBusinessFav(String businessFav) {
        this.businessFav = businessFav;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }
}