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
    private String name;

    public BrandDTO() {
    }

    public BrandDTO(String brandID, String name) {
        this.brandID = brandID;
        this.name = name;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BrandDTO{" + "brandID=" + brandID + ", name=" + name + '}';
    }
    
    
}
