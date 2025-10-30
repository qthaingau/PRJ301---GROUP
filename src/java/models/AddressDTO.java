/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
public class AddressDTO {
    private String addressID;
    private String userID;
    private String recipientName;
    private String phoneNumber;
    private String street; 
    private String ward; 
    private String  district; 
    private String city;
    private boolean isDefault;

    public AddressDTO() {
    }

    public AddressDTO(String addressID, String userID, String recipientName, String phoneNumber, String street, String ward, String district, String city, boolean isDefault) {
        this.addressID = addressID;
        this.userID = userID;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.isDefault = isDefault;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    
    
}
