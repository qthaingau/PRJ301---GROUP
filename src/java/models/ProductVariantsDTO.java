package models;

/**
 * @author TEST
 * DTO (Data Transfer Object) cho Product Variants.
 * Ánh xạ dữ liệu từ bảng 'product_variants' trong cơ sở dữ liệu.
 */
public class ProductVariantsDTO {

    // Các trường ánh xạ trực tiếp từ bảng 'product_variants'
    private String variantID;
    private String productID;
    private String sizeID;
    private String colorID;
    private String SKU; 
    private Integer stockQuantity; // Ánh xạ cột 'stock_quantity'
    private Double price;         
    private Integer itemQuantity;  // Ánh xạ cột 'item_quantity'
    private Double weightKg;      // Ánh xạ cột 'weight_kg' (tên cột CSDL là weight_kg)

    public ProductVariantsDTO() {
    }

    public ProductVariantsDTO(String variantID, String productID, String sizeID, String colorID, String SKU, Integer stockQuantity, Double price, Integer itemQuantity, Double weightKg) {
        this.variantID = variantID;
        this.productID = productID;
        this.sizeID = sizeID;
        this.colorID = colorID;
        this.SKU = SKU;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.itemQuantity = itemQuantity;
        this.weightKg = weightKg;
    }

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

    public String getSizeID() {
        return sizeID;
    }

    public void setSizeID(String sizeID) {
        this.sizeID = sizeID;
    }

    public String getColorID() {
        return colorID;
    }

    public void setColorID(String colorID) {
        this.colorID = colorID;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    @Override
    public String toString() {
        return "ProductVariantsDTO{" + "variantID=" + variantID + ", productID=" + productID + ", sizeID=" + sizeID + ", colorID=" + colorID + ", SKU=" + SKU + ", stockQuantity=" + stockQuantity + ", price=" + price + ", itemQuantity=" + itemQuantity + ", weightKg=" + weightKg + '}';
    }
     
}