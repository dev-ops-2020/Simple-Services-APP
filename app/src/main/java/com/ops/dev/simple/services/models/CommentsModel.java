package com.ops.dev.simple.services.models;
import java.io.Serializable;
public class CommentsModel implements Serializable {

    private String id;
    private String comment;
    private String date;
    private String idUser;
    private String user;
    private String pictureUser;
    private String idEstablishment;

    public CommentsModel() {

    }

    public CommentsModel(String id, String comment, String date, String idUser, String user, String pictureUser, String idEstablishment) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.idUser = idUser;
        this.user = user;
        this.pictureUser = pictureUser;
        this.idEstablishment = idEstablishment;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPictureUser() {
        return pictureUser;
    }

    public void setPictureUser(String pictureUser) {
        this.pictureUser = pictureUser;
    }

    public String getIdEstablishment() {
        return idEstablishment;
    }

    public void setIdEstablishment(String idEstablishment) {
        this.idEstablishment = idEstablishment;
    }


}
