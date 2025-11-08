package models;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.CartItemDTO;
import utils.DBUtils;

public class CartItemDAO {
     

    // Lấy danh sách CartItem theo cartID
    public List<CartItemDTO> getCartItemsByCartID(String cartID) {
        List<CartItemDTO> items = new ArrayList<>();
        String sql = "SELECT ci.cartItemID, ci.variantID, ci.quantity,"
               +" pv.size, pv.color, pv.price, p.productName, pi.imageUrl"
            +"FROM CartItem ci"
            +"JOIN ProductVariant pv ON ci.variantID = pv.variantID"
            +"JOIN Product p ON pv.productID = p.productID"
            +"LEFT JOIN ProductImage pi ON p.productID = pi.productID AND pi.isMain = 1"
            +"WHERE ci.cartID = ?"
            ;

        try ( Connection conn = DBUtils.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItemDTO item = new CartItemDTO();
                item.setCartItemID(rs.getString("cartItemID"));
                item.setVariantID(rs.getString("variantID"));
                item.setProductName(rs.getString("productName"));
                item.setSize(rs.getString("size"));
                item.setColor(rs.getString("color"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setImageUrl(rs.getString("imageUrl"));
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    // Thêm hoặc cập nhật số lượng
    public boolean addOrUpdateItem(String cartID, String variantID, int quantity) {
        String checkSql = "SELECT quantity FROM CartItem WHERE cartID = ? AND variantID = ?";
        String updateSql = "UPDATE CartItem SET quantity = ? WHERE cartID = ? AND variantID = ?";
        String insertSql = "INSERT INTO CartItem (cartItemID, cartID, variantID, quantity) VALUES (?, ?, ?, ?)";

        try {
            // Kiểm tra tồn kho
            String stockSql = "SELECT stock FROM ProductVariant WHERE variantID = ?";
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(stockSql);
            ps.setString(1, variantID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next() || rs.getInt("stock") < quantity) return false;

            // Kiểm tra đã có trong giỏ chưa
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, cartID);
            ps.setString(2, variantID);
            rs = ps.executeQuery();

            if (rs.next()) {
                int newQty = rs.getInt("quantity") + quantity;
                ps = conn.prepareStatement(updateSql);
                ps.setInt(1, newQty);
                ps.setString(2, cartID);
                ps.setString(3, variantID);
                ps.executeUpdate();
            } else {
                String itemID = "CI" + System.currentTimeMillis();
                ps = conn.prepareStatement(insertSql);
                ps.setString(1, itemID);
                ps.setString(2, cartID);
                ps.setString(3, variantID);
                ps.setInt(4, quantity);
                ps.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật số lượng
    public boolean updateQuantity(String cartID, String variantID, int quantity) {
        String sql = "UPDATE CartItem SET quantity = ? WHERE cartID = ? AND variantID = ?";
        
        try (Connection conn = DBUtils.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, cartID);
            ps.setString(3, variantID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa item
    public boolean removeItem(String cartID, String variantID) {
        String sql = "DELETE FROM CartItem WHERE cartID = ? AND variantID = ?";
        try (Connection conn = DBUtils.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartID);
            ps.setString(2, variantID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa toàn bộ giỏ
    public boolean clearCart(String cartID) {
        String sql = "DELETE FROM CartItem WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartID);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getFirstAvailableVariant(String productID) {
    String sql = "SELECT TOP 1 variantID FROM ProductVariant WHERE productID = ? AND stock > 0 ORDER BY variantID";
    try (Connection conn = DBUtils.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, productID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("variantID");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}