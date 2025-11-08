// src/java/models/ProductVariantDTO.java
package models;

public class ProductVariantDTO {

    private String variantID;
    private String productID;
    private String size;
    private String color;
    private int stock;
    private double price;
    private int salesCount;
    private String avatarBase64;  // THÊM MỚI

    // Constructor
    public ProductVariantDTO() {
    }

    public ProductVariantDTO(String variantID, String productID, String size, String color,
            int stock, double price, int salesCount, String avatarBase64) {
        this.variantID = variantID;
        this.productID = productID;
        this.size = size;
        this.color = color;
        this.stock = stock;
        this.price = price;
        this.salesCount = salesCount;
        this.avatarBase64 = avatarBase64;
    }

    // Getters & Setters
    public String getVariantID() {
        return variantID;
    }

    public void setVariantID(String variantID) {
        this.variantID = variantID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }

    // THÊM MỚI
    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }
}
