package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class UsersModel implements Serializable {

    private String id;
    private String name;
    private String alias;
    private String phone;
    private String email;
    private String picture;

    public UsersModel() {

    }

    public UsersModel(String id, String name, String alias, String phone, String email, String picture) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.phone = phone;
        this.email = email;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}