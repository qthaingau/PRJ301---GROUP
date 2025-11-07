package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBUtils;

/**
 *
 * @author TEST
 */
public class UserDAO {

    public UserDAO() {
    }

    public UserDTO getUserByUsername(String username) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM [User] WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserID(rs.getString("userID"));

                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setRole(rs.getString("role"));
                if (rs.getDate("createdAt") != null) {
                    user.setCreatedAt(rs.getDate("createdAt").toLocalDate());
                }
                return user;
            }
        } catch (Exception e) {
        }
        return null;

    }

    public boolean checkLogin(String username, String password) {
        try {
            UserDTO user = getUserByUsername(username);
            if (user != null) {
                return user.getPassword().equals(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    
    public boolean registerUser(String username, String email, String password, String fullName, String phoneNumber) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();

            // Kiểm tra trùng username hoặc email
            String checkSql = "SELECT username FROM [User] WHERE username = ? OR email = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setString(1, username);
            ps.setString(2, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Đã tồn tại username hoặc email
                return false;
            }

            // Tạo userID tự động (ví dụ: U004, U005,...)
            String getMaxId = "SELECT TOP 1 userID FROM [User] ORDER BY userID DESC";
            ps = conn.prepareStatement(getMaxId);
            rs = ps.executeQuery();

            String newId = "U001";
            if (rs.next()) {
                String lastId = rs.getString("userID"); // U003
                int num = Integer.parseInt(lastId.substring(1)) + 1;
                newId = String.format("U%03d", num);
            }

            // Thêm user mới
            String insertSql = "INSERT INTO [User] (userID, username, email, [password], fullName, phoneNumber, role, createdAt) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'customer', GETDATE())";
            ps = conn.prepareStatement(insertSql);
            ps.setString(1, newId);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password); // có thể mã hóa mật khẩu sau
            ps.setString(5, fullName);
            ps.setString(6, phoneNumber);

            result = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
        return result;
    }
    
    public boolean updatePassword(String username, String newPassword) {
    boolean result = false;
    try (Connection conn = DBUtils.getConnection()) {
        String sql = "UPDATE [User] SET password = ? WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newPassword);
        ps.setString(2, username);
        result = ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}

}