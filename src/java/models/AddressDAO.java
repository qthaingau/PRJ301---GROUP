// src/java/dao/AddressDAO.java
package models;


import utils.DBUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.AddressDTO;

public class AddressDAO {

    // Lấy danh sách địa chỉ của user
    public List<AddressDTO> getAddressesByUser(String userID) throws SQLException {
        List<AddressDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Address WHERE userID = ? ORDER BY isDefault DESC, addressID";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy địa chỉ mặc định
    public AddressDTO getDefaultAddress(String userID) throws SQLException {
        String sql = "SELECT TOP 1 * FROM Address WHERE userID = ? AND isDefault = 1";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm địa chỉ mới → trả về ID
    public String addAddress(AddressDTO a) throws SQLException {
        String newID = generateAddressID(a.getUserID());
        String sql = "INSERT INTO Address (addressID, userID, recipientName, phoneNumber, street, ward, district, city, isDefault) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newID);
            ps.setString(2, a.getUserID());
            ps.setString(3, a.getRecipientName());
            ps.setString(4, a.getPhoneNumber());
            ps.setString(5, a.getStreet());
            ps.setString(6, a.getWard());
            ps.setString(7, a.getDistrict());
            ps.setString(8, a.getCity());
            ps.setBoolean(9, a.isIsDefault());
            ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return newID;
    }

    // Cập nhật địa chỉ
    public void updateAddress(AddressDTO a) throws SQLException {
        String sql = "UPDATE Address SET recipientName=?, phoneNumber=?, street=?, ward=?, district=?, city=?, isDefault=? WHERE addressID=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getRecipientName());
            ps.setString(2, a.getPhoneNumber());
            ps.setString(3, a.getStreet());
            ps.setString(4, a.getWard());
            ps.setString(5, a.getDistrict());
            ps.setString(6, a.getCity());
            ps.setBoolean(7, a.isIsDefault());
            ps.setString(8, a.getAddressID());
            ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa địa chỉ (an toàn: không xóa nếu là mặc định duy nhất)
    public boolean deleteAddress(String addressID, String userID) throws SQLException {
        AddressDTO addr = getAddressByID(addressID);
        if (addr == null || !addr.getUserID().equals(userID)) return false;

        try (Connection conn = DBUtils.getConnection()) {
            conn.setAutoCommit(false);

            if (addr.isIsDefault()) {
                String findAlt = "SELECT TOP 1 addressID FROM Address WHERE userID = ? AND addressID <> ?";
                try (PreparedStatement ps = conn.prepareStatement(findAlt)) {
                    ps.setString(1, userID);
                    ps.setString(2, addressID);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            setDefault(rs.getString(1), userID, conn);
                        }
                    }
                }
            }

            String del = "DELETE FROM Address WHERE addressID = ?";
            try (PreparedStatement ps = conn.prepareStatement(del)) {
                ps.setString(1, addressID);
                int rows = ps.executeUpdate();
                conn.commit();
                return rows > 0;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đặt mặc định
    public void setDefault(String addressID, String userID) throws SQLException {
        try (Connection conn = DBUtils.getConnection()) {
            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE Address SET isDefault = 0 WHERE userID = ?");
                 PreparedStatement ps2 = conn.prepareStatement("UPDATE Address SET isDefault = 1 WHERE addressID = ?")) {
                ps1.setString(1, userID);
                ps2.setString(1, addressID);
                ps1.executeUpdate();
                ps2.executeUpdate();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefault(String addressID, String userID, Connection conn) throws SQLException {
        try (PreparedStatement ps1 = conn.prepareStatement("UPDATE Address SET isDefault = 0 WHERE userID = ?");
             PreparedStatement ps2 = conn.prepareStatement("UPDATE Address SET isDefault = 1 WHERE addressID = ?")) {
            ps1.setString(1, userID);
            ps2.setString(1, addressID);
            ps1.executeUpdate();
            ps2.executeUpdate();
        }
    }

    // Lấy theo ID
    public AddressDTO getAddressByID(String addressID) throws SQLException {
        String sql = "SELECT * FROM Address WHERE addressID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tạo ID: A + Uxxx + số thứ tự
    private String generateAddressID(String userID) throws SQLException {
        String sql = "SELECT ISNULL(MAX(CAST(SUBSTRING(addressID, 4, 3) AS INT)), 0) + 1 FROM Address WHERE userID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int seq = rs.getInt(1);
                    return "A" + userID.substring(1) + String.format("%03d", seq);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "A" + userID.substring(1) + "001";
    }

    private AddressDTO mapRow(ResultSet rs) throws SQLException {
        AddressDTO a = new AddressDTO();
        a.setAddressID(rs.getString("addressID"));
        a.setUserID(rs.getString("userID"));
        a.setRecipientName(rs.getString("recipientName"));
        a.setPhoneNumber(rs.getString("phoneNumber"));
        a.setStreet(rs.getString("street"));
        a.setWard(rs.getString("ward"));
        a.setDistrict(rs.getString("district"));
        a.setCity(rs.getString("city"));
        a.setIsDefault(rs.getBoolean("isDefault"));
        return a;
    }
}