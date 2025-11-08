package models;

import models.ProductDTO;

public class ProductVariantDTO {
    private String variantID;
    private String productID;
    private String size;
    private String color;
    private int stock;
    private double price;
    private int salesCount;

    // Constructor
    public ProductVariantDTO() {}
    public ProductVariantDTO(String variantID, String productID, String size, String color,
                             int stock, double price, int salesCount) {
        this.variantID = variantID;
        this.productID = productID;
        this.size = size;
        this.color = color;
        this.stock = stock;
        this.price = price;
        this.salesCount = salesCount;
    }

    // Getters & Setters
    public String getVariantID() { return variantID; }
    public void setVariantID(String variantID) { this.variantID = variantID; }

    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getSalesCount() { return salesCount; }
    public void setSalesCount(int salesCount) { this.salesCount = salesCount; }

}