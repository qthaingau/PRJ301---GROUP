package models;

import utils.DBUtils;
import java.sql.*;
import java.time.LocalDate;
import models.CartDTO;

public class CartDAO {

    public CartDTO getOrCreateCart(String userID) throws SQLException {
        String sql = "SELECT cartID, CAST(createdAt AS DATE) FROM Cart WHERE userID = ?";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, userID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CartDTO(rs.getString(1), userID, rs.getDate(2).toLocalDate());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tạo mới
        return createCart(userID);
    }

    private CartDTO createCart(String userID) throws SQLException {
        String cartID = generateID("Cart", "C");
        String sql = "INSERT INTO Cart (cartID, userID, createdAt) VALUES (?, ?, GETDATE())";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, cartID);
            ps.setString(2, userID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CartDTO(cartID, userID, LocalDate.now());
    }

    String generateID(String table, String prefix) throws SQLException {
        String col = table.equals("Cart") ? "cartID" : "cartItemID";
        String sql = "SELECT TOP 1 " + col + " FROM " + table + " ORDER BY " + col + " DESC";
        int num = 1;
        try {
            Connection conn = DBUtils.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String last = rs.getString(1);
                num = Integer.parseInt(last.substring(prefix.length())) + 1;
            }
            return prefix + String.format("%03d", num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + String.format("%03d", num);
    }
}
