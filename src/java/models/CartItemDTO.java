/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author nguye
 */
public class CartItemDTO {

    private String cartItemID;  // NVARCHAR(50)
    private String cartID;
    private String variantID;
    private int quantity;
    private ProductVariantDTO variant;  // Chứa product + size + color + price

    public CartItemDTO() {}
    public CartItemDTO(String cartItemID, String cartID, String variantID, int quantity, ProductVariantDTO variant) {
        this.cartItemID = cartItemID;
        this.cartID = cartID;
        this.variantID = variantID;
        this.quantity = quantity;
        this.variant = variant;
    }

    // Getters & Setters
    public String getCartItemID() { return cartItemID; }
    public void setCartItemID(String cartItemID) { this.cartItemID = cartItemID; }

    public String getCartID() { return cartID; }
    public void setCartID(String cartID) { this.cartID = cartID; }

    public String getVariantID() { return variantID; }
    public void setVariantID(String variantID) { this.variantID = variantID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity > 0 ? quantity : 1; }

    public ProductVariantDTO getVariant() { return variant; }
    public void setVariant(ProductVariantDTO variant) { this.variant = variant; }

    public double getPrice() { return variant != null ? variant.getPrice() : 0; }
    public String getSize() { return variant != null ? variant.getSize() : ""; }
    public String getColor() { return variant != null ? variant.getColor() : ""; }
    public String getProductName() { return variant != null && variant.getProduct()!= null ? variant.getProduct().getProductName(): ""; }
//    public String getImage() { return variant != null && variant.getProductID()!= null ? variant.getProduct().getImage() : ""; }
}

