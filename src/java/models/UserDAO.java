/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
            String sql = "SELECT * FROM User WHERE username = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UserDTO user = new UserDTO();
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
        }
        return false;

    }
}
