package models;

public class CartItemDTO {

    private String cartItemID;
    private String variantID;
    private String productName;
    private String size;
    private String color;
    private int quantity;
    private double price;
    private String imageUrl;

    // Constructor
    public CartItemDTO() {
    }

    public CartItemDTO(String variantID, String productName, String size, String color, int quantity, double price, String imageUrl) {
        this.variantID = variantID;
        this.productName = productName;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Tính thành tiền
    public double getTotal() {
        return quantity * price;
    }

    // Getters & Setters
    public String getCartItemID() {
        return cartItemID;
    }

    public void setCartItemID(String cartItemID) {
        this.cartItemID = cartItemID;
    }

    public String getVariantID() {
        return variantID;
    }

    public void setVariantID(String variantID) {
        this.variantID = variantID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
