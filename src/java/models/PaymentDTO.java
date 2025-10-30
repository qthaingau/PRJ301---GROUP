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
public class PaymentDTO {
    private String paymentID;        // Mã thanh toán
    private String orderID;          // Mã đơn hàng (FK -> Order)
    private String paymentMethod;    // Phương thức thanh toán (COD, Card, ...)
    private LocalDate paymentDate;   // Ngày thanh toán
    private String paymentStatus;    // Trạng thái: Pending, Completed, Failed, Refunded,...
    private float amount;       // Số tiền thanh toán

    // --- Constructor rỗng ---
    public PaymentDTO() {
    }

    // --- Constructor đầy đủ ---
    public PaymentDTO(String paymentID, String orderID, String paymentMethod,
                      LocalDate paymentDate, String paymentStatus, float amount) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    // --- Getter & Setter ---
    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    // --- ToString (hữu ích khi debug/log) ---
    @Override
    public String toString() {
        return "PaymentDTO{" +
                "paymentID='" + paymentID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", amount=" + amount +
                '}';
    }
}
