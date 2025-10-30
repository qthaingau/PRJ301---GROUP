/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;

/**
 *
 * @author nguye
 */
public class CartDTO {
    private String cartID;
    private String customerID;
    private LocalDate createAt;

    public CartDTO() {
    }

    public CartDTO(String cartID, String customerID, LocalDate createAt) {
        this.cartID = cartID;
        this.customerID = customerID;
        this.createAt = createAt;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }
    
    
}
