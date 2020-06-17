package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class CommentsModel implements Serializable {

    private String id;
    private String comment;
    private String date;
    private String idUser;
    private String nameUser;
    private String pictureUser;
    private String idBusiness;

    public CommentsModel() {

    }

    public CommentsModel(String id, String comment, String date, String idUser, String nameUser, String pictureUser, String idBusiness) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.pictureUser = pictureUser;
        this.idBusiness = idBusiness;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getPictureUser() {
        return pictureUser;
    }

    public void setPictureUser(String pictureUser) {
        this.pictureUser = pictureUser;
    }

    public String getIdBusiness() {
        return idBusiness;
    }

    public void setIdBusiness(String idBusiness) {
        this.idBusiness = idBusiness;
    }
}