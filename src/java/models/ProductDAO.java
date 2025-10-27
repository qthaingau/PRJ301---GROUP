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

    public ArrayList<ProductDTO> getAllProduct() {
        ArrayList<ProductDTO> listProduct = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Product";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setCategoryID("categoryID");
                product.setBrandID(rs.getString("brandID"));
                product.setCreatedAt(rs.getDate("createdAT").toLocalDate());
                product.setIsActive(rs.getBoolean("isActive"));
                listProduct.add(product);
            }
        } catch (Exception e) {
        }
        return listProduct;
    }

    public ProductDTO getProductByID(String productID) {
        try {
            // 1 - Tạo kết nối
            Connection conn = DBUtils.getConnection();
            
            // 2 - Tạo câu lệnh
            String sql = "SELECT * FROM Product WHERE productID = ?";
            
            // 3 - Tạo statement de co the run cau lenh
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productID);
            
            // 4 - Thực thi câu lệnh
            ResultSet rs = pst.executeQuery();

            // 5 - Kiểm tra
            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setCategoryID("categoryID");
                product.setBrandID(rs.getString("brandID"));
                product.setCreatedAt(rs.getDate("createdAT").toLocalDate());
                product.setIsActive(rs.getBoolean("isActive"));
                return product;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<ProductDTO> getProductByName(String productName) {
        List<ProductDTO> listProduct = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Product WHERE productName LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productName);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductID(rs.getString("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setCategoryID("categoryID");
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
        try {
            Connection c = DBUtils.getConnection();
            String sql = "INSERT INTO Product(productID, productName, description, categoryID, brandID, createdAT, isActive)"
                    + "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, product.getProductID());
            pst.setString(2, product.getProductName());
            pst.setString(3, product.getDescription());
        } catch (Exception e) {
        }
    }

    public boolean sortDelete(String productID) {
        try {
            Connection c = DBUtils.getConnection();
            String sql = "UPDATE FROM Product SET isActive = 0"
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
        try {
            Connection c = DBUtils.getConnection();
            String sql = "UPDATE Product "
                    + "SET productName = ?"
                    + ", description = ?"
                    + ", categoryID = ?"
                    + ", brandID = ?"
                    + ", createdAT = ?"
                    + ", isActive = ?"
                    + "WHERE productID = ?";

            PreparedStatement pst = c.prepareStatement(sql);
            pst.setString(1, productDTO.getProductName());
            pst.setString(2, productDTO.getDescription());
            pst.setString(3, productDTO.getCategoryID());
            pst.setString(4, productDTO.getBrandID());
            pst.setDate(5, java.sql.Date.valueOf(productDTO.getCreatedAt()));
            pst.setBoolean(6, productDTO.isIsActive());
            pst.setString(7, productDTO.getProductID());
            
            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
        }
        return false;
    }
}
