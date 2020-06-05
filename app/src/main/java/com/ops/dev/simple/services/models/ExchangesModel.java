package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class ExchangesModel  implements Serializable {
   private String id;
    private String dateObtained;
    private String dateExchange;
    private String idCoupon;
    private String idUser;

    public ExchangesModel() {
    }

    public ExchangesModel(String id, String dateObtained, String dateExchange, String idCoupon, String idUser) {
        this.id = id;
        this.dateObtained = dateObtained;
        this.dateExchange = dateExchange;
        this.idCoupon = idCoupon;
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateObtained() {
        return dateObtained;
    }

    public void setDateObtained(String dateObtained) {
        this.dateObtained = dateObtained;
    }

    public String getDateExchange() {
        return dateExchange;
    }

    public void setDateExchange(String dateExchange) {
        this.dateExchange = dateExchange;
    }

    public String getIdCoupon() {
        return idCoupon;
    }

    public void setIdCoupon(String idCoupon) {
        this.idCoupon = idCoupon;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
