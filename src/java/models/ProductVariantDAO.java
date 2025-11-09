// src/java/models/ProductVariantDAO.java
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ProductVariantDAO {

    public ProductVariantDAO() {
    }

    // ---------------------- GET ALL VARIANTS ----------------------
    public ArrayList<ProductVariantDTO> getAllVariants() {
        ArrayList<ProductVariantDTO> listVariant = new ArrayList<>();
        String sql = "SELECT * FROM ProductVariant";  // THÊM CỘT
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql);  ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                ProductVariantDTO variant = new ProductVariantDTO();
                variant.setVariantID(rs.getString("variantID"));
                variant.setProductID(rs.getString("productID"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStock(rs.getInt("stock"));
                variant.setPrice(rs.getDouble("price"));
                variant.setSalesCount(rs.getInt("salesCount"));
                variant.setVariantImage(rs.getString("variantImage"));  // THÊM
                listVariant.add(variant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listVariant;
    }

    // ---------------------- GET VARIANT BY ID ----------------------
    public ProductVariantDTO getVariantByID(String variantID) {
        String sql = "SELECT * FROM ProductVariant WHERE variantID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, variantID);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ProductVariantDTO variant = new ProductVariantDTO();
                    variant.setVariantID(rs.getString("variantID"));
                    variant.setProductID(rs.getString("productID"));
                    variant.setSize(rs.getString("size"));
                    variant.setColor(rs.getString("color"));
                    variant.setStock(rs.getInt("stock"));
                    variant.setPrice(rs.getDouble("price"));
                    variant.setSalesCount(rs.getInt("salesCount"));
                    variant.setVariantImage(rs.getString("variantImage"));  // THÊM
                    return variant;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------- GET BY PRODUCT ID (Customer & Admin) ----------------------
    public List<ProductVariantDTO> getActiveVariantsByProductID(String productID) {
        List<ProductVariantDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductVariant WHERE productID = ? AND stock > 0";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, productID);
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ProductVariantDTO v = new ProductVariantDTO();
                    v.setVariantID(rs.getString("variantID"));
                    v.setProductID(rs.getString("productID"));
                    v.setSize(rs.getString("size"));
                    v.setColor(rs.getString("color"));
                    v.setStock(rs.getInt("stock"));
                    v.setPrice(rs.getDouble("price"));
                    v.setSalesCount(rs.getInt("salesCount"));
                    v.setVariantImage(rs.getString("variantImage"));  // THÊM
                    list.add(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductVariantDTO> getAllVariantsByProductID(String productID) {
        List<ProductVariantDTO> list = new ArrayList<>();
        String sql = "SELECT variantID, productID, size, color, stock, price, salesCount, variantImage "
                + "FROM ProductVariant WHERE productID = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getConnection();  // BẮT BUỘC CÓ DBUTILS
            if (con == null) {
                System.out.println(">>> LỖI: Không kết nối được DB!");
                return list;
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, productID);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductVariantDTO v = new ProductVariantDTO(
                        rs.getString("variantID"),
                        rs.getString("productID"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getInt("stock"),
                        rs.getDouble("price"),
                        rs.getInt("salesCount"),
                        rs.getString("variantImage")
                );
                list.add(v);
            }

            System.out.println(">>> Tìm thấy " + list.size() + " variant cho productID = " + productID);

        } catch (Exception e) {
            System.out.println(">>> LỖI DAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return list;
    }
    
// Trong ProductVariantDAO

public List<ProductVariantDTO> filterVariant(String keyword) throws Exception {
    List<ProductVariantDTO> list = new ArrayList<>();

    // Tìm theo: variantID, productID, size, color
    String sql = "SELECT variantID, productID, size, color, stock, price, variantImage "
               + "FROM ProductVariant "
               + "WHERE variantID LIKE ? "
               + "   OR productID LIKE ? "
               + "   OR size LIKE ? "
               + "   OR color LIKE ?";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement stm = conn.prepareStatement(sql)) {

        String search = "%" + keyword + "%";

        stm.setString(1, search); // variantID
        stm.setString(2, search); // productID
        stm.setString(3, search); // size
        stm.setString(4, search); // color

        try (ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                ProductVariantDTO v = new ProductVariantDTO();
                v.setVariantID(rs.getString("variantID"));
                v.setProductID(rs.getString("productID"));
                v.setSize(rs.getString("size"));
                v.setColor(rs.getString("color"));
                v.setStock(rs.getInt("stock"));
                v.setPrice(rs.getDouble("price"));
                v.setVariantImage(rs.getString("variantImage"));
                // KHÔNG set isActive vì bảng/DTO không có
                list.add(v);
            }
        }
    }

    return list;
}


    // ---------------------- INSERT ----------------------
    public boolean insert(ProductVariantDTO v) {
        String sql = "INSERT INTO ProductVariant(variantID, productID, size, color, stock, price, salesCount, variantImage) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, v.getVariantID());
            pst.setString(2, v.getProductID());
            pst.setString(3, v.getSize());
            pst.setString(4, v.getColor());
            pst.setInt(5, v.getStock());
            pst.setDouble(6, v.getPrice());
            pst.setInt(7, v.getSalesCount());
            pst.setString(8, v.getVariantImage());  // THÊM

            int rows = pst.executeUpdate();
            if (rows > 0) {
                updateIsActiveByStock(v.getProductID());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- UPDATE ----------------------
    public boolean update(ProductVariantDTO v) {
        String sql = "UPDATE ProductVariant "
                + "SET size = ?, color = ?, stock = ?, price = ?, salesCount = ?, variantImage = ? "
                + "WHERE variantID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, v.getSize());
            pst.setString(2, v.getColor());
            pst.setInt(3, v.getStock());
            pst.setDouble(4, v.getPrice());
            pst.setInt(5, v.getSalesCount());
            pst.setString(6, v.getVariantImage());  // THÊM
            pst.setString(7, v.getVariantID());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                updateIsActiveByStock(v.getProductID());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- DELETE (set stock = 0) ----------------------
    public boolean delete(String variantID, String productID) {
        String sql = "UPDATE ProductVariant SET stock = 0 WHERE variantID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, variantID);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                updateIsActiveByStock(productID);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- UPDATE STOCK & SALES ----------------------
    public boolean updateStockAndSales(String variantID, int quantity) {
        String sql = "UPDATE ProductVariant "
                + "SET stock = stock - ?, salesCount = salesCount + ? "
                + "WHERE variantID = ? AND stock >= ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, quantity);
            pst.setInt(2, quantity);
            pst.setString(3, variantID);
            pst.setInt(4, quantity);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                ProductVariantDTO v = getVariantByID(variantID);
                if (v != null) {
                    updateIsActiveByStock(v.getProductID());
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- UPDATE PRODUCT.ISACTIVE ----------------------
    public void updateIsActiveByStock(String productID) {
        String sql = "UPDATE Product "
                + "SET isActive = CASE "
                + " WHEN COALESCE((SELECT SUM(stock) FROM ProductVariant WHERE productID = ?), 0) > 0 "
                + " THEN 1 ELSE 0 END "
                + "WHERE productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, productID);
            pst.setString(2, productID);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}