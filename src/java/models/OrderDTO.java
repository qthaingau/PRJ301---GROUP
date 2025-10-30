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
public class OrderDTO {
    private String orderID;           // ID đơn hàng
    private String userName;          // Người đặt hàng
    private LocalDate orderDate;           // Ngày tạo đơn
    private float totalAmount;   // Tổng tiền
    private String status;            // Trạng thái đơn hàng
    private String shippingAddress;   // Địa chỉ giao hàng
    private String paymentMethod;     // Phương thức thanh toán

    // --- Constructor rỗng (bắt buộc cho Hibernate hoặc Bean) ---
    public OrderDTO() {
    }

    // --- Constructor đầy đủ ---
    public OrderDTO(String orderID, String userName, LocalDate orderDate, float totalAmount,
                    String status, String shippingAddress, String paymentMethod) {
        this.orderID = orderID;
        this.userName = userName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
    }

    // --- Getter & Setter ---
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // --- ToString (hữu ích khi debug hoặc log) ---
    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderID='" + orderID + '\'' +
                ", userName='" + userName + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
