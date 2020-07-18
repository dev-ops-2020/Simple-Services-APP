package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class CommentsModel implements Serializable {

    private String id;
    private String comment;
    private String date;
    private String userId;
    private String userAlias;
    private String userPicture;
    private String businessId;

    public CommentsModel() {

    }

    public CommentsModel(String id, String comment, String date, String userId, String userAlias, String userPicture, String businessId) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.userId = userId;
        this.userAlias = userAlias;
        this.userPicture = userPicture;
        this.businessId = businessId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}