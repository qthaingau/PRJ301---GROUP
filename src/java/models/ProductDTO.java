package models;

import java.time.LocalDate;

/**
 *
 * @author TEST
 */
public class ProductDTO {
    private String productID;
    private String productName;
    private String description;
    private String categoryID;
    private String brandID;
    private LocalDate createdAt;
    private boolean isActive;
    private String productImage; // Trường mới

    public ProductDTO() {
    }

    // Constructor đã được cập nhật để bao gồm productImage
    public ProductDTO(String productID, String productName, String description, String categoryID, String brandID, LocalDate createdAt, boolean isActive, String productImage) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.categoryID = categoryID;
        this.brandID = brandID;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.productImage = productImage; // Thêm productImage
    }

    // Constructor cũ, có thể giữ lại hoặc xóa tùy vào nhu cầu sử dụng
    public ProductDTO(String productID, String productName, String description, String categoryID, String brandID, LocalDate createdAt, boolean isActive) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.categoryID = categoryID;
        this.brandID = brandID;
        this.createdAt = createdAt;
        this.isActive = isActive;
        // productImage sẽ là null
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getBrandID() {
        return brandID;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // Getter cho productImage
    public String getProductImage() {
        return productImage;
    }

    // Setter cho productImage
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "ProductDTO{" + "productID=" + productID + ", productName=" + productName + ", description=" + description + ", categoryID=" + categoryID + ", brandID=" + brandID + ", createdAt=" + createdAt + ", isActive=" + isActive + ", productImage=" + productImage + '}'; // Cập nhật toString
    }
}