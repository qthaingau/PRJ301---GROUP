/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author TEST
 */
public class ProductImageDTO {
    private String imageID;
    private String productID;
    private String imageUrl;
    private boolean isMain;

    public ProductImageDTO() {
    }

    public ProductImageDTO(String imageID, String productID, String imageUrl, boolean isMain) {
        this.imageID = imageID;
        this.productID = productID;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIsMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    @Override
    public String toString() {
        return "ProductImageDTO{" + "imageID=" + imageID + ", productID=" + productID + ", imageUrl=" + imageUrl + ", isMain=" + isMain + '}';
    }
}
