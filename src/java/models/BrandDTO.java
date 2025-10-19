/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author TEST
 */
public class BrandDTO {
    private String brandID;
    private String brandName;
    private String origin;

    public BrandDTO() {
    }

    public BrandDTO(String brandID, String brandName, String origin) {
        this.brandID = brandID;
        this.brandName = brandName;
        this.origin = origin;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "BrandDTO{" + "brandID=" + brandID + ", brandName=" + brandName + ", origin=" + origin + '}';
    }
}
