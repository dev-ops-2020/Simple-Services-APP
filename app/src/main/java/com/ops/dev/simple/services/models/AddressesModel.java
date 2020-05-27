package com.ops.dev.simple.services.models;

public class AddressesModel {

    private String id;
    private String name;
    private String countryRegion;
    private String streetAddress;
    private String city;
    private String stateProvinceRegion;
    private String zipCode;
    private String phone;
    private String deliveryInstructions;
    private String securityCode;
    private String idUser;

    public AddressesModel() {

    }

    public AddressesModel(String id, String name, String countryRegion, String streetAddress, String city, String stateProvinceRegion, String zipCode, String phone, String deliveryInstructions, String securityCode, String idUser) {
        this.id = id;
        this.name = name;
        this.countryRegion = countryRegion;
        this.streetAddress = streetAddress;
        this.city = city;
        this.stateProvinceRegion = stateProvinceRegion;
        this.zipCode = zipCode;
        this.phone = phone;
        this.deliveryInstructions = deliveryInstructions;
        this.securityCode = securityCode;
        this.idUser = idUser;
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

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvinceRegion() {
        return stateProvinceRegion;
    }

    public void setStateProvinceRegion(String stateProvinceRegion) {
        this.stateProvinceRegion = stateProvinceRegion;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
