package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql);  ResultSet rs = pst.executeQuery()) {

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

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql);  ResultSet rs = pst.executeQuery()) {

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

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, categoryID);

            try ( ResultSet rs = pst.executeQuery()) {
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
    public ArrayList<CategoryDTO> getCategoryByName(String categoryName) {
        ArrayList<CategoryDTO> listCategory = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE categoryName LIKE ? AND isActive = 1";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + categoryName + "%");

            try ( ResultSet rs = pst.executeQuery()) {
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

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

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
    String sql = "UPDATE Category SET categoryName = ?, sportType = ?, isActive = ? WHERE categoryID = ?";

    try (Connection conn = DBUtils.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

        // Set các tham số cho câu lệnh SQL
        pst.setString(1, category.getCategoryName());
        pst.setString(2, category.getSportType());
        pst.setBoolean(3, category.getIsActive());
        pst.setString(4, category.getCategoryID());

        // Thực thi câu lệnh UPDATE
        int rows = pst.executeUpdate();
        
        // Nếu câu lệnh UPDATE thành công, đồng bộ trạng thái sản phẩm
        if (rows > 0) {
            // Nếu category bị vô hiệu hóa (isActive = false)
            if (!category.getIsActive()) {
                // Nếu category bị vô hiệu hóa, cập nhật tất cả sản phẩm liên kết với category này thành inactive
                updateProductsToInactive(category.getCategoryID(), conn);
            } else {
                // Nếu category được kích hoạt lại (isActive = true), cập nhật tất cả sản phẩm liên kết với category này thành active
                updateProductsToActive(category.getCategoryID(), conn);
            }
            return true;  // Trả về true nếu cập nhật thành công
        }
    } catch (Exception e) {
        e.printStackTrace();  // Log lỗi nếu có
    }
    return false;  // Trả về false nếu cập nhật không thành công
}

// ---------------------- CẬP NHẬT TRẠNG THÁI SẢN PHẨM THEO CATEGORY ----------------------
private void updateProductsToInactive(String categoryID, Connection conn) throws SQLException {
    // Truy vấn để lấy tất cả các sản phẩm có categoryID tương ứng và cập nhật thành inactive
    String sql = "UPDATE Product SET isActive = 0 WHERE categoryID = ?";
    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, categoryID);  // categoryID
        pst.executeUpdate();  // Thực thi câu lệnh update
    }
}

// ---------------------- CẬP NHẬT TRẠNG THÁI SẢN PHẨM ACTIVE THEO CATEGORY ----------------------
private void updateProductsToActive(String brandID, Connection conn) throws SQLException {
    // Truy vấn để lấy tất cả các sản phẩm có liên kết với brandID, kiểm tra trạng thái của brand và tổng stock
    String sql = "SELECT p.productID, b.isActive AS brandActive, SUM(v.stock) AS totalStock "
               + "FROM Product p "
               + "JOIN Brand b ON p.brandID = b.brandID "  // Kiểm tra trạng thái brand
               + "JOIN ProductVariant v ON p.productID = v.productID "  // Kiểm tra stock của variant
               + "WHERE p.categoryID = ? "
               + "GROUP BY p.productID, b.isActive";  // Nhóm theo productID và brand's isActive

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, brandID);  // brandID
        ResultSet rs = pst.executeQuery();

        // Lặp qua tất cả các sản phẩm của brandID
        while (rs.next()) {
            String productID = rs.getString("productID");
            boolean isBrandActive = rs.getBoolean("brandActive");
            int totalStock = rs.getInt("totalStock");

            // Kiểm tra nếu brand là Active và có stock > 0 thì cập nhật sản phẩm thành Active
            if (isBrandActive && totalStock > 0) {
                String updateSql = "UPDATE Product SET isActive = 1 WHERE productID = ?";
                try (PreparedStatement updatePst = conn.prepareStatement(updateSql)) {
                    updatePst.setString(1, productID);
                    updatePst.executeUpdate();
                    System.out.println("Product " + productID + " updated to Active.");
                }
            } else {
                // Nếu không đủ điều kiện, cập nhật trạng thái sản phẩm thành Inactive
                String updateSql = "UPDATE Product SET isActive = 0 WHERE productID = ?";
                try (PreparedStatement updatePst = conn.prepareStatement(updateSql)) {
                    updatePst.setString(1, productID);
                    updatePst.executeUpdate();
                    System.out.println("Product " + productID + " updated to Inactive.");
                }
            }
        }
    }
}

    // ---------------------- DEACTIVATE CATEGORY (SOFT DELETE) ----------------------
    public boolean delete(String categoryID) {
        String sql = "UPDATE Category SET isActive = 0 WHERE categoryID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, categoryID);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- FILTER CATEGORY THEO KEYWORD ----------------------
// Tìm theo ID, tên hoặc sportType, chỉ lấy isActive = 1
    public List<CategoryDTO> filterCategory(String keyword) {
        List<CategoryDTO> listCategory = new ArrayList<>();

        String sql = "SELECT * FROM Category "
                + "WHERE categoryName LIKE ? "
                + "OR categoryID LIKE ? "
                + "OR sportType LIKE ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            pst.setString(1, like);
            pst.setString(2, like);
            pst.setString(3, like);

            try ( ResultSet rs = pst.executeQuery()) {
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

}
