package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    // (OPTIONAL) Nếu em muốn control luôn isActive khi insert:
    // public boolean insert(BrandDTO brand) {
    //     String sql = "INSERT INTO Brand(brandID, brandName, origin, isActive) VALUES(?, ?, ?, ?)";
    //     ...
    // }
    // ---------------------- CẬP NHẬT BRAND ----------------------
    public boolean update(BrandDTO brand) {
        String sql = "UPDATE Brand "
                + "SET brandName = ?, origin = ? "
                + "WHERE brandID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brand.getBrandName());
            pst.setString(2, brand.getOrigin());
            pst.setString(3, brand.getBrandID());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- DEACTIVATE BRAND (SOFT DELETE) ----------------------
    public boolean delete(String brandID) {
        String sql = "UPDATE Brand SET isActive = 0 WHERE brandID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, brandID);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
