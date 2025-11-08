package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 * DAO for Brand table.
 */
public class BrandDAO {

    public BrandDAO() {
    }

    // ---------------------- LẤY TẤT CẢ BRAND (kể cả bị deactivate) ----------------------
    // Dùng cho ADMIN nếu muốn xem cả brand đã ẩn
    public ArrayList<BrandDTO> getAllBrand() {
        ArrayList<BrandDTO> listBrand = new ArrayList<>();
        String sql = "SELECT * FROM Brand";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql);  ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                BrandDTO brand = new BrandDTO();
                brand.setBrandID(rs.getString("brandID"));
                brand.setBrandName(rs.getString("brandName"));
                brand.setOrigin(rs.getString("origin"));
                brand.setIsActive(rs.getBoolean("isActive")); // <-- map isActive
                listBrand.add(brand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBrand;
    }

    // ---------------------- LẤY BRAND ACTIVE (isActive = 1) ----------------------
    public List<BrandDTO> getActiveBrands() {
        List<BrandDTO> listBrand = new ArrayList<>();
        String sql = "SELECT * FROM Brand WHERE isActive = 1";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql);  ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                BrandDTO brand = new BrandDTO();
                brand.setBrandID(rs.getString("brandID"));
                brand.setBrandName(rs.getString("brandName"));
                brand.setOrigin(rs.getString("origin"));
                brand.setIsActive(rs.getBoolean("isActive"));
                listBrand.add(brand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBrand;
    }

    // ---------------------- LẤY BRAND THEO ID ----------------------
    public BrandDTO getBrandByID(String brandID) {
        String sql = "SELECT * FROM Brand WHERE brandID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brandID);

            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    BrandDTO brand = new BrandDTO();
                    brand.setBrandID(rs.getString("brandID"));
                    brand.setBrandName(rs.getString("brandName"));
                    brand.setOrigin(rs.getString("origin"));
                    brand.setIsActive(rs.getBoolean("isActive"));
                    return brand;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------- TÌM BRAND THEO TÊN (chỉ brand active) ----------------------
    public List<BrandDTO> getBrandByName(String brandName) {
        List<BrandDTO> listBrand = new ArrayList<>();
        String sql = "SELECT * FROM Brand WHERE brandName LIKE ? AND isActive = 1";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + brandName + "%");

            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BrandDTO brand = new BrandDTO();
                    brand.setBrandID(rs.getString("brandID"));
                    brand.setBrandName(rs.getString("brandName"));
                    brand.setOrigin(rs.getString("origin"));
                    brand.setIsActive(rs.getBoolean("isActive"));
                    listBrand.add(brand);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBrand;
    }

    // ---------------------- FILTER BRAND THEO KEYWORD ----------------------
    public List<BrandDTO> filterBrand(String keyword) {
        List<BrandDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Brand "
                + "WHERE brandID   LIKE ? "
                + "   OR brandName LIKE ? "
                + "   OR origin    LIKE ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            pst.setString(1, like);
            pst.setString(2, like);
            pst.setString(3, like);

            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    BrandDTO b = new BrandDTO();
                    b.setBrandID(rs.getString("brandID"));
                    b.setBrandName(rs.getString("brandName"));
                    b.setOrigin(rs.getString("origin"));
                    b.setIsActive(rs.getBoolean("isActive"));
                    list.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------------- THÊM BRAND MỚI ----------------------
    public boolean insert(BrandDTO brand) {
        // DB đã có DEFAULT isActive = 1, nên không cần truyền isActive nếu thêm brand mới bình thường
        String sql = "INSERT INTO Brand(brandID, brandName, origin) VALUES(?, ?, ?)";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brand.getBrandID());
            pst.setString(2, brand.getBrandName());
            pst.setString(3, brand.getOrigin());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- CẬP NHẬT BRAND ----------------------
    public boolean update(BrandDTO brand) {
        String sql = "UPDATE Brand SET brandName = ?, origin = ?, isActive = ? WHERE brandID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {
            // Cập nhật thông tin Brand
            pst.setString(1, brand.getBrandName());   // brandName
            pst.setString(2, brand.getOrigin());       // origin
            pst.setBoolean(3, brand.isIsActive());    // isActive
            pst.setString(4, brand.getBrandID());     // brandID (for WHERE clause)

            // Thực thi câu lệnh update và lấy số dòng bị ảnh hưởng
            int rows = pst.executeUpdate();

            // Nếu có ít nhất một dòng bị ảnh hưởng (Brand đã được cập nhật)
            if (rows > 0) {
                // Nếu brand bị vô hiệu hóa (isActive = false)
                if (!brand.isIsActive()) {
                    // Nếu brand bị vô hiệu hóa, cập nhật tất cả sản phẩm liên kết với brandID này thành inactive
                    updateProductsToInactive(brand.getBrandID(), conn);
                } else {
                    // Nếu brand được kích hoạt lại (isActive = true), cập nhật tất cả sản phẩm liên kết với brandID này thành active
                    updateProductsToActive(brand.getBrandID(), conn);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();  // In ra lỗi nếu có
        }
        return false;
    }

// Cập nhật trạng thái của tất cả sản phẩm có liên kết với brandID thành inactive
    public void updateProductsToInactive(String brandID, Connection conn) throws SQLException {
        String sql = "UPDATE Product SET isActive = 0 WHERE brandID = ?";
        try ( PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, brandID);  // brandID
            pst.executeUpdate();  // Thực thi câu lệnh update
        }
    }

// Cập nhật trạng thái của tất cả sản phẩm có liên kết với brandID thành active
    public void updateProductsToActive(String brandID, Connection conn) throws SQLException {
    // Truy vấn để lấy tất cả các sản phẩm có liên kết với brandID, kiểm tra trạng thái của category và tổng stock
    String sql = "SELECT p.productID, c.isActive AS categoryActive, SUM(v.stock) AS totalStock "
               + "FROM Product p "
               + "JOIN Category c ON p.categoryID = c.categoryID "  // Kiểm tra trạng thái category
               + "JOIN ProductVariant v ON p.productID = v.productID "  // Kiểm tra stock của variant
               + "WHERE p.brandID = ? "
               + "GROUP BY p.productID, c.isActive";  // Nhóm theo productID và category's isActive

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, brandID);  // brandID
        ResultSet rs = pst.executeQuery();

        // Lặp qua tất cả các sản phẩm của brandID
        while (rs.next()) {
            String productID = rs.getString("productID");
            boolean isCategoryActive = rs.getBoolean("categoryActive");
            int totalStock = rs.getInt("totalStock");

            // Nếu category là Active và có stock > 0, cập nhật trạng thái sản phẩm thành Active
            if (isCategoryActive && totalStock > 0) {
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


    // ---------------------- DEACTIVATE BRAND (SOFT DELETE) ----------------------
    public boolean delete(String brandID) {
        // Cập nhật trạng thái isActive của Brand thành false (soft delete)
        String sql = "UPDATE Brand SET isActive = 0 WHERE brandID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brandID);  // brandID
            int rows = pst.executeUpdate();

            // Nếu có dòng bị ảnh hưởng, tức là cập nhật thành công
            if (rows > 0) {
                // Cập nhật tất cả sản phẩm liên kết với brandID này thành inactive
                updateProductsToInactive(brandID, conn);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
