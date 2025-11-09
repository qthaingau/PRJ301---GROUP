package models;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import models.CartDTO;
import models.CartItemDTO;
import utils.DBUtils;

public class CartDAO {

    public CartDTO getCartByUser(String userID) {
        CartDTO cart = null;
        String cartID = getCartIDByUser(userID);
        if (cartID == null) {
            return new CartDTO(); // Trả cart rỗng
        }
        cart = new CartDTO();
        cart.setCartID(cartID);
        cart.setUserID(userID);

        List<CartItemDTO> itemList = new ArrayList<>();
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT ci.cartItemID, ci.variantID, ci.quantity, pv.price, p.productName, pv.size, pv.color, pv.variantImage "
                    + "FROM CartItem ci "
                    + "JOIN ProductVariant pv ON ci.variantID = pv.variantID "
                    + "JOIN Product p ON pv.productID = p.productID "
                    + "WHERE ci.cartID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cartID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItemDTO ci = new CartItemDTO();
                ci.setCartItemID(rs.getString("cartItemID"));
                ci.setVariantID(rs.getString("variantID"));
                ci.setQuantity(rs.getInt("quantity"));
                ci.setPrice(rs.getDouble("price"));
                ci.setProductName(rs.getString("productName"));
                ci.setSize(rs.getString("size"));
                ci.setColor(rs.getString("color"));
                ci.setImageUrl(rs.getString("variantImage"));
                itemList.add(ci);
            }
            cart.setItems(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cart;
    }

    public String getCartIDByUser(String userID) {
        String cartID = null;
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT cartID FROM Cart WHERE userID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cartID = rs.getString("cartID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartID;
    }

    public void insertCart(String cartID, String userID) {
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "INSERT INTO Cart(cartID, userID) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cartID);
            ps.setString(2, userID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean itemExists(String cartID, String variantID) {
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "SELECT * FROM CartItem WHERE cartID = ? AND variantID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cartID);
            ps.setString(2, variantID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addOrUpdateItem(String userID, String variantID, int quantity) {
        String cartID = getCartIDByUser(userID);
        if (cartID == null) {
            cartID = "CART_" + userID;
            insertCart(cartID, userID);
        }

        try ( Connection conn = DBUtils.getConnection()) {
            if (itemExists(cartID, variantID)) {
                String sql = "UPDATE CartItem SET quantity = quantity + ? WHERE cartID = ? AND variantID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, quantity);
                ps.setString(2, cartID);
                ps.setString(3, variantID);
                ps.executeUpdate();
            } else {
                String sql = "INSERT INTO CartItem(cartItemID, cartID, variantID, quantity) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, "CI" + System.currentTimeMillis());
                ps.setString(2, cartID);
                ps.setString(3, variantID);
                ps.setInt(4, quantity);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(String cartItemID) {
        try ( Connection conn = DBUtils.getConnection()) {
            String sql = "DELETE FROM CartItem WHERE cartItemID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cartItemID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy cart theo user, nếu chưa có thì tạo mới
    public CartDTO getOrCreateCartID(String userID) {
        CartDTO cart = getCartByUserID(userID);
        if (cart != null) return cart;

            try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Cart(userID) OUTPUT INSERTED.cartID, VALUES(?)")) {
                ps.setString(1, userID);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new CartDTO(rs.getString("cartID"), userID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
    }
    
    public CartDTO getCartByUserID(String userID) {
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT cartID, userID, createdAt FROM Cart WHERE userID=?")) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CartDTO(rs.getString("cartID"), rs.getString("userID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
