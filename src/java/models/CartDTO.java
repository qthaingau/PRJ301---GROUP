package models;

import java.time.LocalDate;
import java.util.List;

public class CartDTO {

    private String cartID;
    private String userID;
    private List<CartItemDTO> items;

    public CartDTO() {
    }

    public CartDTO(String cartID, String userID) {
        this.cartID = cartID;
        this.userID = userID;
    }

    // Getters & Setters
    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    // Tổng tiền
    public double getTotal() {
        if (items == null) {
            return 0;
        }
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}
