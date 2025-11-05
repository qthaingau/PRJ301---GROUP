package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 * DAO for ProductVariant table.
 */
public class ProductVariantDAO {

    public ProductVariantDAO() {
    }

    // ---------------------- GET ALL VARIANTS ----------------------
    public ArrayList<ProductVariantDTO> getAllVariants() {
        ArrayList<ProductVariantDTO> listVariant = new ArrayList<>();
        String sql = "SELECT * FROM ProductVariant";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                ProductVariantDTO variant = new ProductVariantDTO();
                variant.setVariantID(rs.getString("variantID"));
                variant.setProductID(rs.getString("productID"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStock(rs.getInt("stock"));
                variant.setPrice(rs.getDouble("price"));
                variant.setSalesCount(rs.getInt("salesCount"));
                listVariant.add(variant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listVariant;
    }

    // ---------------------- GET VARIANT BY VARIANT ID ----------------------
    public ProductVariantDTO getVariantByID(String variantID) {
        
        String sql = "SELECT * FROM ProductVariant WHERE variantID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, variantID);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ProductVariantDTO variant = new ProductVariantDTO();
                    variant.setVariantID(rs.getString("variantID"));
                    variant.setProductID(rs.getString("productID"));
                    variant.setSize(rs.getString("size"));
                    variant.setColor(rs.getString("color"));
                    variant.setStock(rs.getInt("stock"));
                    variant.setPrice(rs.getDouble("price"));
                    variant.setSalesCount(rs.getInt("salesCount"));
                    return variant;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------- GET FIRST VARIANT BY PRODUCT ID ----------------------
    public ProductVariantDTO getVariantByProductID(String productID) {
        String sql = "SELECT TOP 1 * FROM ProductVariant WHERE productID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, productID);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ProductVariantDTO variant = new ProductVariantDTO();
                    variant.setVariantID(rs.getString("variantID"));
                    variant.setProductID(rs.getString("productID"));
                    variant.setSize(rs.getString("size"));
                    variant.setColor(rs.getString("color"));
                    variant.setStock(rs.getInt("stock"));
                    variant.setPrice(rs.getDouble("price"));
                    variant.setSalesCount(rs.getInt("salesCount"));
                    return variant;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------- UPDATE PRODUCT.ISACTIVE BY TOTAL STOCK ----------------------
    public void updateIsActiveByStock(String productID) {
        String sql =
                "UPDATE Product " +
                "SET isActive = CASE " +
                "    WHEN COALESCE((SELECT SUM(stock) FROM ProductVariant WHERE productID = ?), 0) > 0 " +
                "         THEN 1 " +
                "    ELSE 0 " +
                "END " +
                "WHERE productID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, productID);
            pst.setString(2, productID);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- GET VARIANTS BY PRODUCT ID ----------------------
    public List<ProductVariantDTO> getVariantsByProductID(String productID) {
        List<ProductVariantDTO> listVariant = new ArrayList<>();
        String sql = "SELECT * FROM ProductVariant WHERE productID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, productID);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ProductVariantDTO variant = new ProductVariantDTO();
                    variant.setVariantID(rs.getString("variantID"));
                    variant.setProductID(rs.getString("productID"));
                    variant.setSize(rs.getString("size"));
                    variant.setColor(rs.getString("color"));
                    variant.setStock(rs.getInt("stock"));
                    variant.setPrice(rs.getDouble("price"));
                    variant.setSalesCount(rs.getInt("salesCount"));
                    listVariant.add(variant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listVariant;
    }

    // ---------------------- INSERT NEW VARIANT ----------------------
    public boolean insert(ProductVariantDTO v) {
        String sql = "INSERT INTO ProductVariant(variantID, productID, size, color, stock, price, salesCount) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, v.getVariantID());
            pst.setString(2, v.getProductID());
            pst.setString(3, v.getSize());
            pst.setString(4, v.getColor());
            pst.setInt(5, v.getStock());
            pst.setDouble(6, v.getPrice());
            pst.setInt(7, v.getSalesCount());

            int rows = pst.executeUpdate();

            if (rows > 0) {
                // sync Product.isActive based on total stock
                updateIsActiveByStock(v.getProductID());
                return true;
            }
        } catch (Exception e) {
            // check for unique constraint violation on (productID, size, color) if needed
            e.printStackTrace();
        }
        return false;
    }

    // ---------------------- UPDATE VARIANT ----------------------
    public boolean update(ProductVariantDTO v) {
        String sql = "UPDATE ProductVariant "
                   + "SET size = ?, color = ?, stock = ?, price = ?, salesCount = ? "
                   + "WHERE variantID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, v.getSize());
            pst.setString(2, v.getColor());
            pst.setInt(3, v.getStock());
            pst.setDouble(4, v.getPrice());
            pst.setInt(5, v.getSalesCount());
            pst.setString(6, v.getVariantID());

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

    // ---------------------- DELETE VARIANT ----------------------
    public boolean delete(String variantID, String productID) {
        String sql = "DELETE FROM ProductVariant WHERE variantID = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

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

    // ---------------------- UPDATE STOCK & SALES WHEN ORDERING ----------------------
    /**
     * Decrease stock and increase salesCount when an order is placed.
     * Ensures stock is not negative.
     */
    public boolean updateStockAndSales(String variantID, int quantity) {
        String sql =
                "UPDATE ProductVariant " +
                "SET stock = stock - ?, salesCount = salesCount + ? " +
                "WHERE variantID = ? AND stock >= ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, quantity);
            pst.setInt(2, quantity);
            pst.setString(3, variantID);
            pst.setInt(4, quantity);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                // get productID to sync isActive
                ProductVariantDTO v = getVariantByID(variantID);
                if (v != null) {
                    updateIsActiveByStock(v.getProductID());
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // not enough stock or error
    }
}
