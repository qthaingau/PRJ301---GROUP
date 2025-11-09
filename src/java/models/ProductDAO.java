/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author TEST
 */
public class ProductDAO {

    public ProductDAO() {
    }

    public boolean toggleStatus(String productID) {
        // 1. Lấy trạng thái hiện tại + tổng stock của tất cả variant thuộc product
        String checkSql
                = "SELECT isActive, "
                + "       COALESCE((SELECT SUM(stock) FROM ProductVariant WHERE productID = ?), 0) AS totalStock "
                + "FROM Product "
                + "WHERE productID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, productID);
            checkStmt.setString(2, productID);

            try ( ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("toggleStatus: product not found, id = " + productID);
                    return false;
                }

                boolean currentActive = rs.getBoolean("isActive");
                int totalStock = rs.getInt("totalStock");

                System.out.println("toggleStatus CHECK: productID=" + productID
                        + ", currentActive=" + currentActive
                        + ", totalStock=" + totalStock);

                // 2. Nếu đang Inactive mà hết hàng (totalStock <= 0) -> KHÔNG cho bật lại
                if (!currentActive && totalStock <= 0) {
                    System.out.println("toggleStatus BLOCKED: no stock for product " + productID);
                    return false;
                }

                // 3. Tính trạng thái mới (đảo ngược)
                boolean newStatus = !currentActive;

                String updateSql = "UPDATE Product SET isActive = ? WHERE productID = ?";
                try ( PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setBoolean(1, newStatus);
                    updateStmt.setString(2, productID);

                    int rows = updateStmt.executeUpdate();
                    System.out.println("toggleStatus UPDATE: productID=" + productID
                            + ", newStatus=" + newStatus + ", rows=" + rows);
                    return rows > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ProductDTO> getAllProduct() {
        ArrayList<ProductDTO> listProduct = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            // Cập nhật SQL nếu bạn chỉ chọn các cột cụ thể. Tuy nhiên, SELECT * vẫn hoạt động.
            String sql = "SELECT * FROM Product";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setCategoryID(rs.getString("categoryID"));
                product.setBrandID(rs.getString("brandID"));
                product.setCreatedAt(rs.getDate("createdAT").toLocalDate());
                product.setIsActive(rs.getBoolean("isActive"));

                // ✅ BỔ SUNG: Lấy và thiết lập productImage
                product.setProductImage(rs.getString("productImage"));

                listProduct.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Nên in lỗi để dễ debug hơn
        }
        return listProduct;
    }

    public ProductDTO getProductByID(String productID) {
        try {
            //Tạo kết nối
            Connection conn = DBUtils.getConnection();

            //Tạo truy vấn
            String sql = "SELECT * FROM Product WHERE productID = ?";

            //Tạo statement de co the run cau lenh
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productID);

            //Thực thi truy vấn
            ResultSet rs = pst.executeQuery();

            //Kiểm tra
            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setCategoryID(rs.getString("categoryID"));  // ✅ lấy từ ResultSet
                product.setBrandID(rs.getString("brandID"));
                product.setCreatedAt(rs.getDate("createdAT").toLocalDate());
                product.setIsActive(rs.getBoolean("isActive"));
                product.setProductImage(rs.getString("productImage"));
                return product;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<ProductDTO> filterProduct(String keyword) {
        List<ProductDTO> listProduct = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Product "
                    + "WHERE productName LIKE ? "
                    + "   OR productID LIKE ? "
                    + "   OR categoryID LIKE ? "
                    + "   OR brandID LIKE ? "
                    + "   OR createdAt LIKE ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            pst.setString(3, "%" + keyword + "%");
            pst.setString(4, "%" + keyword + "%");
            pst.setString(5, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setCategoryID(rs.getString("categoryID"));  // ✅ lấy từ ResultSet
                product.setBrandID(rs.getString("brandID"));
                product.setCreatedAt(rs.getDate("createdAT").toLocalDate());
                product.setIsActive(rs.getBoolean("isActive"));
                listProduct.add(product);
            }
        } catch (Exception e) {
        }
        return listProduct;
    }

    public boolean insert(ProductDTO product) {
        // ✅ Cập nhật SQL: Thêm productImage và dấu ?
        String sql = "INSERT INTO Product(productID, productName, description, categoryID, brandID, createdAT, isActive, productImage) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection c = DBUtils.getConnection();  PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, product.getProductID());
            pst.setString(2, product.getProductName());
            pst.setString(3, product.getDescription());
            pst.setString(4, product.getCategoryID());
            pst.setString(5, product.getBrandID());

            // Nếu ngày tạo chưa có thì lấy ngày hiện tại
            java.time.LocalDate createdAt = product.getCreatedAt() != null
                    ? product.getCreatedAt()
                    : java.time.LocalDate.now();
            pst.setDate(6, java.sql.Date.valueOf(createdAt));

            // Kiểm tra stock của productID trong ProductVariant
            boolean isActive = checkStockStatus(product.getProductID());
            pst.setBoolean(7, isActive);

            // ✅ BỔ SUNG: Thiết lập giá trị cho productImage (vị trí tham số thứ 8)
            pst.setString(8, product.getProductImage());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra tổng stock của ProductVariant, nếu > 0 thì còn hàng.
     */
    private boolean checkStockStatus(String productID) {
        String sql = "SELECT COALESCE(SUM(stock), 0) AS totalStock "
                + "FROM ProductVariant WHERE productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, productID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalStock") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật Product.isActive dựa vào tổng stock trong ProductVariant
    public void updateIsActiveByStock(String productID) {
        String sql
                = "UPDATE Product "
                + "SET isActive = CASE "
                + "    WHEN COALESCE((SELECT SUM(stock) FROM ProductVariant WHERE productID = ?), 0) > 0 "
                + "         THEN 1 "
                + "    ELSE 0 "
                + "END "
                + "WHERE productID = ?";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, productID);
            pst.setString(2, productID);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean delete(String productID) {
        try {
            Connection c = DBUtils.getConnection();
            String sql = "UPDATE Product SET isActive = 0"
                    + "WHERE userID = ?";

            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, productID);

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean update(ProductDTO productDTO) {
        try ( Connection c = DBUtils.getConnection()) {
            String sql = "UPDATE Product "
                    + "SET productName = ?, "
                    + "    description = ?, "
                    + "    categoryID = ?, "
                    + "    brandID = ?, "
                    + "    isActive = ?, "
                    + "    productImage = ? " // ✅ BỔ SUNG: Cột productImage
                    + "WHERE productID = ?";

            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, productDTO.getProductName());
            pst.setString(2, productDTO.getDescription());
            pst.setString(3, productDTO.getCategoryID());
            pst.setString(4, productDTO.getBrandID());
            pst.setBoolean(5, productDTO.isIsActive());

            // ✅ BỔ SUNG: Thiết lập giá trị cho productImage (vị trí tham số thứ 6)
            pst.setString(6, productDTO.getProductImage());

            // Cập nhật vị trí của productID (vị trí tham số thứ 7)
            pst.setString(7, productDTO.getProductID());

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
