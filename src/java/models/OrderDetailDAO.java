package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class OrderDetailDAO {

    public OrderDetailDAO() {
    }

    // 1. Lấy tất cả OrderDetail
    public ArrayList<OrderDetailDTO> getAllOrderDetails() {
        ArrayList<OrderDetailDTO> list = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM OrderDetail";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setOrderDetailID(rs.getString("orderDetailID"));
                detail.setOrderID(rs.getString("orderID"));
                detail.setVariantID(rs.getString("variantID"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPricePerUnit(rs.getFloat("pricePerUnit"));

                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy OrderDetail theo ID
    public OrderDetailDTO getOrderDetailByID(String orderDetailID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM OrderDetail WHERE orderDetailID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, orderDetailID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setOrderDetailID(rs.getString("orderDetailID"));
                detail.setOrderID(rs.getString("orderID"));
                detail.setVariantID(rs.getString("variantID"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPricePerUnit(rs.getFloat("pricePerUnit"));
                return detail;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Lấy danh sách OrderDetail theo orderID (tức là tất cả sản phẩm trong một đơn)
    public List<OrderDetailDTO> getOrderDetailsByOrderID(String orderID) {
        List<OrderDetailDTO> list = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM OrderDetail WHERE orderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, orderID);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setOrderDetailID(rs.getString("orderDetailID"));
                detail.setOrderID(rs.getString("orderID"));
                detail.setVariantID(rs.getString("variantID"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPricePerUnit(rs.getFloat("pricePerUnit"));
                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Tìm kiếm OrderDetail theo variantID (có thể dùng để debug hoặc quản lý hàng)
    public List<OrderDetailDTO> searchByVariantID(String variantID) {
        List<OrderDetailDTO> list = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM OrderDetail WHERE variantID LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + variantID + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setOrderDetailID(rs.getString("orderDetailID"));
                detail.setOrderID(rs.getString("orderID"));
                detail.setVariantID(rs.getString("variantID"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPricePerUnit(rs.getFloat("pricePerUnit"));
                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5. Thêm mới OrderDetail
    public boolean insert(OrderDetailDTO detail) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "INSERT INTO OrderDetail(orderDetailID, orderID, variantID, quantity, pricePerUnit) "
                       + "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, detail.getOrderDetailID());
            pst.setString(2, detail.getOrderID());
            pst.setString(3, detail.getVariantID());
            pst.setInt(4, detail.getQuantity());
            pst.setFloat(5, detail.getPricePerUnit());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Cập nhật OrderDetail
    public boolean update(OrderDetailDTO detail) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE OrderDetail "
                       + "SET orderID = ?, variantID = ?, quantity = ?, pricePerUnit = ? "
                       + "WHERE orderDetailID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, detail.getOrderID());
            pst.setString(2, detail.getVariantID());
            pst.setInt(3, detail.getQuantity());
            pst.setFloat(4, detail.getPricePerUnit());
            pst.setString(5, detail.getOrderDetailID());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 7. Xóa OrderDetail
    public boolean delete(String orderDetailID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM OrderDetail WHERE orderDetailID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, orderDetailID);

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
