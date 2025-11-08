/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartItemDAO {

    public List<CartItemDTO> getItemsByCartID(String cartID) throws SQLException {
        List<CartItemDTO> items = new ArrayList<>();
        String sql
                = "SELECT ci.cartItemID, ci.cartID, ci.variantID, ci.quantity,pv.size,"
                + " pv.color, pv.price, pv.stock,p.productID, p.productName, p.image"
                + "FROM CartItem ci"
                + "JOIN ProductVariant pv ON ci.variantID = pv.variantID"
                + "JOIN Product p ON pv.productID = p.productID"
                + " WHERE ci.cartID = ?";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cartID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setImage(rs.getString("image"));

                ProductVariantDTO variant = new ProductVariantDTO();
                variant.setVariantID(rs.getString("variantID"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setPrice(rs.getDouble("price"));
                variant.setStock(rs.getInt("stock"));
                variant.setProduct(product);

                CartItemDTO item = new CartItemDTO(
                        rs.getString("cartItemID"),
                        rs.getString("cartID"),
                        rs.getString("variantID"),
                        rs.getInt("quantity"),
                        variant
                );
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    // Trong addItem
    public void addItem(String cartID, String variantID, int quantity) throws SQLException {
        CartItemDTO existing = getItemByVariant(cartID, variantID);
        if (existing != null) {
            updateQuantity(existing.getCartItemID(), existing.getQuantity() + quantity);
        } else {
            String cartItemID = generateCartItemID();
            String sql = "INSERT INTO CartItem (cartItemID, cartID, variantID, quantity) VALUES (?, ?, ?, ?)";
            try {
                Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, cartItemID);
                ps.setString(2, cartID);
                ps.setString(3, variantID);
                ps.setInt(4, quantity);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

// Tạo CI001, CI002...
    private String generateCartItemID() throws SQLException {
        int nextNum = 1;
        String sql = "SELECT TOP 1 cartItemID FROM CartItem ORDER BY cartItemID DESC";
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            
            if (rs.next()) {
                String lastID = rs.getString("cartItemID");
                try {
                    nextNum = Integer.parseInt(lastID.substring(2)) + 1; // CI001 → bỏ "CI"
                } catch (Exception e) {
                    nextNum = 1;
                }
            }
            return String.format("CI%03d", nextNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("CI%03d", nextNum);
    }

    public void updateQuantity(String cartItemID, int quantity) throws SQLException {
        String sql = "UPDATE CartItem SET quantity = ? WHERE cartItemID = ?";
        try{
            Connection conn = DBUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, quantity);
        ps.setString(2, cartItemID);
        ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItem(String cartItemID) throws SQLException {
        String sql = "DELETE FROM CartItem WHERE cartItemID = ?";
        try{
            Connection conn = DBUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cartItemID);
        ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CartItemDTO getItemByVariant(String cartID, String variantID) throws SQLException {
        String sql = "SELECT * FROM CartItem WHERE cartID = ? AND variantID = ?";
        try{
            Connection conn = DBUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cartID);
        ps.setString(2, variantID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new CartItemDTO(rs.getString("cartItemID"), cartID, variantID, rs.getInt("quantity"), null);
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
