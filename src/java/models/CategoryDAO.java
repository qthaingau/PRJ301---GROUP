package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CategoryDAO {

    public CategoryDAO() {
    }

    // ---------------------- LẤY TẤT CẢ CATEGORY (kể cả inactive) ----------------------
    // Dùng cho ADMIN
    public ArrayList<CategoryDTO> getAllCategory() {
        ArrayList<CategoryDTO> listCategory = new ArrayList<>();
        String sql = "SELECT * FROM Category";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setCategoryID(rs.getString("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setSportType(rs.getString("sportType"));
                category.setIsActive(rs.getBoolean("isActive"));
                listCategory.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCategory;
    }

    // ---------------------- LẤY CATEGORY ACTIVE (isActive = 1) ----------------------
    // Dùng cho dropdown chọn category khi thêm / sửa product
    public List<CategoryDTO> getActiveCategories() {
        List<CategoryDTO> listCategory = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE isActive = 1";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setCategoryID(rs.getString("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setSportType(rs.getString("sportType"));
                category.setIsActive(rs.getBoolean("isActive"));
                listCategory.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCategory;
    }

    // ---------------------- LẤY CATEGORY THEO ID ----------------------
    public CategoryDTO getCategoryByID(String categoryID) {
        String sql = "SELECT * FROM Category WHERE categoryID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, categoryID);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    CategoryDTO category = new CategoryDTO();
                    category.setCategoryID(rs.getString("categoryID"));
                    category.setCategoryName(rs.getString("categoryName"));
                    category.setSportType(rs.getString("sportType"));
                    category.setIsActive(rs.getBoolean("isActive"));
                    return category;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------- TÌM CATEGORY THEO TÊN (chỉ active) ----------------------
    public List<CategoryDTO> getCategoryByName(String categoryName) {
        List<CategoryDTO> listCategory = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE categoryName LIKE ? AND isActive = 1";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + categoryName + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    CategoryDTO category = new CategoryDTO();
                    category.setCategoryID(rs.getString("categoryID"));
                    category.setCategoryName(rs.getString("categoryName"));
                    category.setSportType(rs.getString("sportType"));
                    category.setIsActive(rs.getBoolean("isActive"));
                    listCategory.add(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCategory;
    }

    // ---------------------- THÊM CATEGORY MỚI ----------------------
    public boolean insert(CategoryDTO category) {
        // DB có DEFAULT isActive = 1 nên không cần truyền isActive
        String sql = "INSERT INTO Category(categoryID, categoryName, sportType) VALUES(?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, category.getCategoryID());
            pst.setString(2, category.getCategoryName());
            pst.setString(3, category.getSportType());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- CẬP NHẬT CATEGORY ----------------------
    public boolean update(CategoryDTO category) {
        String sql = "UPDATE Category "
                   + "SET categoryName = ?, "
                   + "    sportType = ? "
                   + "WHERE categoryID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, category.getCategoryName());
            pst.setString(2, category.getSportType());
            pst.setString(3, category.getCategoryID());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- DEACTIVATE CATEGORY (SOFT DELETE) ----------------------
    public boolean delete(String categoryID) {
        String sql = "UPDATE Category SET isActive = 0 WHERE categoryID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, categoryID);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
