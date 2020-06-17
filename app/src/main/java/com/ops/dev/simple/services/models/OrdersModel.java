package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class OrdersModel implements Serializable {

   private String id;
    private String date;
    private String subTotal;
    private String total;
    private String status;
    private String idProduct;
    private String idAddress;
    private String idUser;
    private String idEstablishment;

    public OrdersModel() {

    }

    public OrdersModel(String id, String date, String subTotal, String total, String status, String idProduct, String idAddress, String idUser, String idEstablishment) {
        this.id = id;
        this.date = date;
        this.subTotal = subTotal;
        this.total = total;
        this.status = status;
        this.idProduct = idProduct;
        this.idAddress = idAddress;
        this.idUser = idUser;
        this.idEstablishment = idEstablishment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdEstablishment() {
        return idEstablishment;
    }

    public void setIdEstablishment(String idEstablishment) {
        this.idEstablishment = idEstablishment;
    }
}