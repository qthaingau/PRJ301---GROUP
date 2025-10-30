/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author nguye
 */
public class OrderDetailDTO {
     private String orderDetailID;   // ID chi tiết đơn hàng
    private String orderID;         // Mã đơn hàng (FK -> Order)
    private String variantID;       // Mã biến thể sản phẩm (FK -> ProductVariant)
    private int quantity;           // Số lượng mua
    private float pricePerUnit; // Giá của mỗi sản phẩm tại thời điểm mua

    // --- Constructor rỗng ---
    public OrderDetailDTO() {
    }

    // --- Constructor đầy đủ ---
    public OrderDetailDTO(String orderDetailID, String orderID, String variantID, int quantity, float pricePerUnit) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.variantID = variantID;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    // --- Getter & Setter ---
    public String getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getVariantID() {
        return variantID;
    }

    public void setVariantID(String variantID) {
        this.variantID = variantID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    // --- Tính tổng tiền của dòng sản phẩm (quantity * pricePerUnit) ---
    public float getTotalPrice() {
        return quantity * pricePerUnit;
    }

    // --- ToString (hữu ích khi debug hoặc log) ---
    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "orderDetailID='" + orderDetailID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", variantID='" + variantID + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}
