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

//    public boolean checkLogin(String txtUsername, String txtPassword) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//
//    public UserDTO getUserById(String txtUsername) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
    
    
  public UserDAO() {
    }

    
    public UserDTO getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM [User] WHERE username = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUserID(rs.getString("userID"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("fullName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setRole(rs.getString("role"));

                
                if (rs.getDate("createdAt") != null) {
                    user.setCreatedAt(rs.getDate("createdAt").toLocalDate());
                }

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    
    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        UserDTO user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
