package models;

import utils.DBUtils;
import java.sql.*;
import java.time.LocalDate;
import models.CartDTO;

public class CartDAO {

    // Lấy cartID theo userID (nếu chưa có → tạo mới)
    public String getOrCreateCartID(String userID) {
        String cartID = null;
        String sql = "SELECT cartID FROM Cart WHERE userID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cartID = rs.getString("cartID");
            } else {
                cartID = "C" + System.currentTimeMillis();
                String insert = "INSERT INTO Cart (cartID, userID) VALUES (?, ?)";
                try ( PreparedStatement ps2 = conn.prepareStatement(insert)) {
                    ps2.setString(1, cartID);
                    ps2.setString(2, userID);
                    ps2.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartID;
    }
    
    
}
