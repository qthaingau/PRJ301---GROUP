package models;

/**
 * @author TEST
 * DTO (Data Transfer Object) cho Product.
 * Ánh xạ dữ liệu từ bảng 'products' trong cơ sở dữ liệu.
 */
public class ProductDTO {

    // Các trường ánh xạ trực tiếp từ bảng 'products'
    private String productID;
    private String name;
    private String description;
    private String material;
    private String gender;
    private String brandID;
    private String categoryID;
    private String sportTypeID;
    private String reviewUrl;
    
    // Trường được thêm vào: giả định nó tồn tại trong bảng products
    private String createdAt; 

    // Constructor rỗng (Default Constructor)
    public ProductDTO() {
    }

    // Constructor đầy đủ tham số
    public ProductDTO(String productID, String name, String description, String material, String gender, String brandID, String categoryID, String sportTypeID, String reviewUrl, String createdAt) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.material = material;
        this.gender = gender;
        this.brandID = brandID;
        this.categoryID = categoryID;
        this.sportTypeID = sportTypeID;
        this.reviewUrl = reviewUrl;
        this.createdAt = createdAt;
    }

    // Getters
    
    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMaterial() {
        return material;
    }

    public String getGender() {
        return gender;
    }

    public String getBrandID() {
        return brandID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getSportTypeID() {
        return sportTypeID;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    // Setters

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBrandID(String brandID) {
        this.brandID = brandID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public void setSportTypeID(String sportTypeID) {
        this.sportTypeID = sportTypeID;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    // Phương thức toString() hỗ trợ debugging
    @Override
    public String toString() {
        return "ProductDTO{" +
                "productID='" + productID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", material='" + material + '\'' +
                ", gender='" + gender + '\'' +
                ", brandID='" + brandID + '\'' +
                ", categoryID='" + categoryID + '\'' +
                ", sportTypeID='" + sportTypeID + '\'' +
                ", reviewUrl='" + reviewUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}