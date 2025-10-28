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

    // Lấy tất cả category
    public ArrayList<CategoryDTO> getAllCategory() {
        ArrayList<CategoryDTO> listCategory = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Category";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setCategoryID(rs.getString("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setSportType(rs.getString("sportType"));
                category.setParentCategoryID(rs.getString("parentCategoryID"));

                listCategory.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCategory;
    }

    // Lấy category theo ID (PK)
    public CategoryDTO getCategoryByID(String categoryID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Category WHERE categoryID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, categoryID);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setCategoryID(rs.getString("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setSportType(rs.getString("sportType"));
                category.setParentCategoryID(rs.getString("parentCategoryID"));
                return category;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tìm category theo tên (LIKE)
    public List<CategoryDTO> getCategoryByName(String categoryName) {
        List<CategoryDTO> listCategory = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Category WHERE categoryName LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + categoryName + "%"); // dùng wildcard để search gần đúng

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                CategoryDTO category = new CategoryDTO();
                category.setCategoryID(rs.getString("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setSportType(rs.getString("sportType"));
                category.setParentCategoryID(rs.getString("parentCategoryID"));

                listCategory.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCategory;
    }

    // Thêm mới category
    public boolean insert(CategoryDTO category) {
        try {
            Connection c = DBUtils.getConnection();
            String sql = "INSERT INTO Category(categoryID, categoryName, sportType, parentCategoryID) "
                       + "VALUES(?, ?, ?, ?)";

            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, category.getCategoryID());
            pst.setString(2, category.getCategoryName());
            pst.setString(3, category.getSportType());
            pst.setString(4, category.getParentCategoryID());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa cứng (DELETE thật). 
    // Nếu bạn muốn "soft delete" thì bảng phải có cột isActive / status. Hiện chưa có => mình để DELETE.
    public boolean delete(String categoryID) {
        try {
            Connection c = DBUtils.getConnection();
            String sql = "DELETE FROM Category WHERE categoryID = ?";

            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, categoryID);

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật category
    public boolean update(CategoryDTO category) {
        try {
            Connection c = DBUtils.getConnection();
            String sql = "UPDATE Category "
                       + "SET categoryName = ?, "
                       + "sportType = ?, "
                       + "parentCategoryID = ? "
                       + "WHERE categoryID = ?";

            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, category.getCategoryName());
            pst.setString(2, category.getSportType());
            pst.setString(3, category.getParentCategoryID());
            pst.setString(4, category.getCategoryID());

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
