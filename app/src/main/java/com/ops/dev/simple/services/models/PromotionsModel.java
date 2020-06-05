package com.ops.dev.simple.services.models;

import java.io.Serializable;

public class PromotionsModel  implements Serializable {

   private String id;
    private String tittle;
    private String description;
    private String discount;
    private String picture;
    private String datePublishing;
    private String dateExpiration;
    private String status;
    private String idCategory;
    private String idEstablishment;
    private String  establishmentName;
    private String establishmentIco;

    public PromotionsModel() {
    }

    public PromotionsModel(String id, String tittle, String description, String discount, String picture, String datePublishing, String dateExpiration, String status, String idCategory, String idEstablishment, String establishmentName, String establishmentIco) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.discount = discount;
        this.picture = picture;
        this.datePublishing = datePublishing;
        this.dateExpiration = dateExpiration;
        this.status = status;
        this.idCategory = idCategory;
        this.idEstablishment = idEstablishment;
        this.establishmentName = establishmentName;
        this.establishmentIco = establishmentIco;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDatePublishing() {
        return datePublishing;
    }

    public void setDatePublishing(String datePublishing) {
        this.datePublishing = datePublishing;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdEstablishment() {
        return idEstablishment;
    }

    public void setIdEstablishment(String idEstablishment) {
        this.idEstablishment = idEstablishment;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getEstablishmentIco() {
        return establishmentIco;
    }

    public void setEstablishmentIco(String establishmentIco) {
        this.establishmentIco = establishmentIco;
    }
}
