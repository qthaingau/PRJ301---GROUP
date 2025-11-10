// src/java/models/CartItemDAO.java
package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartItemDAO {

    // ✅ Lấy danh sách CartItem - ưu tiên variantImage
    public List<CartItemDTO> getCartItemsByCartID(String cartID) {
        List<CartItemDTO> items = new ArrayList<>();
        
        // ✅ SQL: Ưu tiên variantImage, nếu null thì lấy productImage
        String sql = "SELECT ci.cartItemID, ci.variantID, ci.quantity, "
                   + "pv.size, pv.color, pv.price, "
                   + "p.productName, "
                   + "COALESCE(pv.variantImage, p.productImage) AS imageUrl "
                   + "FROM CartItem ci "
                   + "JOIN ProductVariant pv ON ci.variantID = pv.variantID "
                   + "JOIN Product p ON pv.productID = p.productID "
                   + "WHERE ci.cartID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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
            
            System.out.println("✅ Loaded " + items.size() + " items for cartID: " + cartID);
            
        } catch (Exception e) {
            System.err.println("❌ Error loading cart items:");
            e.printStackTrace();
        }
        return items;
    }

    // Thêm hoặc cập nhật số lượng
    public boolean addOrUpdateItem(String cartID, String variantID, int quantity) {
    Connection conn = null;
    PreparedStatement checkStmt = null;
    PreparedStatement stockStmt = null;
    PreparedStatement updateStmt = null;
    PreparedStatement insertStmt = null;
    ResultSet rs = null;

    try {
        conn = DBUtils.getConnection();
        System.out.println("🔍 Step 1: Checking stock for variant: " + variantID);
        
        // 1. Kiểm tra tồn kho
        String stockSql = "SELECT stock FROM ProductVariant WHERE variantID = ?";
        stockStmt = conn.prepareStatement(stockSql);
        stockStmt.setString(1, variantID);
        rs = stockStmt.executeQuery();
        
        if (!rs.next()) {
            System.out.println("❌ Variant not found: " + variantID);
            return false;
        }
        
        int availableStock = rs.getInt("stock");
        System.out.println("📦 Available stock: " + availableStock + ", Requested quantity: " + quantity);
        
        if (availableStock < quantity) {
            System.out.println("❌ Insufficient stock. Available: " + availableStock + ", Requested: " + quantity);
            return false;
        }

        // 2. Kiểm tra đã có trong giỏ chưa
        System.out.println("🔍 Step 2: Checking if item exists in cart: " + cartID);
        String checkSql = "SELECT quantity FROM CartItem WHERE cartID = ? AND variantID = ?";
        checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, cartID);
        checkStmt.setString(2, variantID);
        rs = checkStmt.executeQuery();

        if (rs.next()) {
            // Đã có -> cập nhật số lượng
            int currentQty = rs.getInt("quantity");
            int newQty = currentQty + quantity;
            
            System.out.println("🛒 Item exists - Current: " + currentQty + ", New total: " + newQty);
            
            if (newQty > availableStock) {
                System.out.println("❌ Total quantity would exceed stock. Available: " + availableStock + ", Would be: " + newQty);
                return false;
            }
            
            String updateSql = "UPDATE CartItem SET quantity = ? WHERE cartID = ? AND variantID = ?";
            updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, newQty);
            updateStmt.setString(2, cartID);
            updateStmt.setString(3, variantID);
            int updated = updateStmt.executeUpdate();
            
            System.out.println("✅ Updated cart item. Rows affected: " + updated);
            return updated > 0;
        } else {
            // Chưa có -> thêm mới
            System.out.println("🆕 Item not in cart, inserting new...");
            String itemID = "CI" + System.currentTimeMillis();
            String insertSql = "INSERT INTO CartItem (cartItemID, cartID, variantID, quantity) VALUES (?, ?, ?, ?)";
            insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, itemID);
            insertStmt.setString(2, cartID);
            insertStmt.setString(3, variantID);
            insertStmt.setInt(4, quantity);
            int inserted = insertStmt.executeUpdate();
            
            System.out.println("✅ Inserted new cart item. Rows affected: " + inserted);
            return inserted > 0;
        }
    } catch (Exception e) {
        System.err.println("❌ Error in addOrUpdateItem:");
        e.printStackTrace();
        return false;
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) { }
        try { if (checkStmt != null) checkStmt.close(); } catch (Exception e) { }
        try { if (stockStmt != null) stockStmt.close(); } catch (Exception e) { }
        try { if (updateStmt != null) updateStmt.close(); } catch (Exception e) { }
        try { if (insertStmt != null) insertStmt.close(); } catch (Exception e) { }
        try { if (conn != null) conn.close(); } catch (Exception e) { }
    }
}

    // Cập nhật số lượng
    public boolean updateQuantity(String cartID, String variantID, int quantity) {
        if (quantity <= 0) {
            return removeItem(cartID, variantID);
        }
        
        String sql = "UPDATE CartItem SET quantity = ? WHERE cartID = ? AND variantID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cartID);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy variant đầu tiên còn hàng
    public String getFirstAvailableVariant(String productID) {
        String sql = "SELECT TOP 1 variantID FROM ProductVariant WHERE productID = ? AND stock > 0 ORDER BY variantID";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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