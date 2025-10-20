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

    public List<ProductDTO> getAllProduct() {
        List<ProductDTO> listProduct = new ArrayList<>();
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
}
