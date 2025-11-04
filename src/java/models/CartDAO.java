package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartDAO {

    public CartDAO() {
    }

    //  Lấy tất cả Cart
    public ArrayList<CartDTO> getAllCart() {
        ArrayList<CartDTO> listCart = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Cart";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getString("cartID"));
                cart.setCustomerID(rs.getString("userID"));
                cart.setCreateAt(rs.getDate("createdAt").toLocalDate());
                listCart.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCart;
    }

    //  Lấy Cart theo ID (PK)
    public CartDTO getCartByID(String cartID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Cart WHERE cartID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, cartID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getString("cartID"));
                cart.setCustomerID(rs.getString("userID"));
                cart.setCreateAt(rs.getDate("createdAt").toLocalDate());
                return cart;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔍 Tìm kiếm giỏ hàng theo userID (LIKE)
    public List<CartDTO> getCartByUser(String userID) {
        List<CartDTO> listCart = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Cart WHERE userID LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + userID + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                CartDTO cart = new CartDTO();
                cart.setCartID(rs.getString("cartID"));
                cart.setCustomerID(rs.getString("userID"));
                cart.setCreateAt(rs.getDate("createdAt").toLocalDate());
                listCart.add(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCart;
    }

    //  Thêm mới Cart
    public boolean insert(CartDTO cart) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "INSERT INTO Cart(cartID, userID, createdAt) VALUES (?, ?, GETDATE())";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, cart.getCartID());
            pst.setString(2, cart.getCustomerID());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Xóa Cart
    public boolean delete(String cartID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM Cart WHERE cartID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, cartID);

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✏️ Cập nhật Cart (VD: cập nhật userID nếu cần)
    public boolean update(CartDTO cart) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE Cart SET userID = ? WHERE cartID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, cart.getCustomerID());
            pst.setString(2, cart.getCartID());

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
