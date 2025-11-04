package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class OrderDAO {

    public OrderDAO() {
    }

    // Lấy tất cả order
    public ArrayList<OrderDTO> getAllOrder() {
        ArrayList<OrderDTO> listOrder = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM [Order]";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderID(rs.getString("orderID"));
                order.setUserName(rs.getString("userID"));
                order.setOrderDate(rs.getDate("orderDate").toLocalDate());
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setPaymentMethod(rs.getString("paymentID"));
                listOrder.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOrder;
    }

    //  Lấy order theo ID
    public OrderDTO getOrderByID(String orderID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM [Order] WHERE orderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, orderID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderID(rs.getString("orderID"));
                order.setUserName(rs.getString("userID"));
                order.setOrderDate(rs.getDate("orderDate").toLocalDate());
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setPaymentMethod(rs.getString("paymentID"));
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //  Tìm order theo userID hoặc status
    public List<OrderDTO> searchOrder(String keyword) {
        List<OrderDTO> listOrder = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM [Order] WHERE userID LIKE ? OR status LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderID(rs.getString("orderID"));
                order.setUserName(rs.getString("userID"));
                order.setOrderDate(rs.getDate("orderDate").toLocalDate());
                order.setStatus(rs.getString("status"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setPaymentMethod(rs.getString("paymentID"));
                listOrder.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOrder;
    }

    //  Thêm order mới
    public boolean insert(OrderDTO order) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "INSERT INTO [Order](orderID, userID, orderDate, status, totalAmount, paymentID) "
                       + "VALUES(?, ?, GETDATE(), ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, order.getOrderID());
            pst.setString(2, order.getUserName());
            pst.setString(3, order.getStatus());
            pst.setDouble(4, order.getTotalAmount());
            pst.setString(5, order.getPaymentMethod());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Cập nhật order
    public boolean update(OrderDTO order) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE [Order] SET status = ?, totalAmount = ?, paymentID = ? WHERE orderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, order.getStatus());
            pst.setDouble(2, order.getTotalAmount());
            pst.setString(3, order.getPaymentMethod());
            pst.setString(4, order.getOrderID());

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Xóa order
    public boolean delete(String orderID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM [Order] WHERE orderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, orderID);
            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
