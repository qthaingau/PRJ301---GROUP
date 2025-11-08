/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author nguye
 */
public class CartDTO {

    private String cartID;  // NVARCHAR(50)
    private String userID;
    private LocalDate createdAt;
    private List<CartItemDTO> items;

    public CartDTO() {
    }

    public CartDTO(String cartID, String userID, LocalDate createdAt) {
        this.cartID = cartID;
        this.userID = userID;
        this.createdAt = createdAt;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
