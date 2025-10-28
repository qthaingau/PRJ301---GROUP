package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 * DAO cho bảng ProductImage
 * Chịu trách nhiệm quản lý ảnh sản phẩm (gallery, thumbnail chính,...)
 */
public class ProductImageDAO {

    public ProductImageDAO() {
    }

    // -----------------------------------------------------------
    // 1. Lấy tất cả ảnh của một sản phẩm (để show gallery)
    // -----------------------------------------------------------
    public List<ProductImageDTO> getImagesByProductID(String productID) {
        List<ProductImageDTO> listImages = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM ProductImage WHERE productID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productID);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                ProductImageDTO img = new ProductImageDTO();
                img.setImageID(rs.getString("imageID"));
                img.setProductID(rs.getString("productID"));
                img.setImageUrl(rs.getString("imageUrl"));
                img.setIsMain(rs.getBoolean("isMain"));

                listImages.add(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listImages;
    }

    // -----------------------------------------------------------
    // 2. Lấy ảnh chính (thumbnail) của 1 sản phẩm
    //    - Dùng để hiển thị ở trang danh sách sản phẩm
    // -----------------------------------------------------------
    public ProductImageDTO getMainImageByProductID(String productID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM ProductImage WHERE productID = ? AND isMain = 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productID);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                ProductImageDTO img = new ProductImageDTO();
                img.setImageID(rs.getString("imageID"));
                img.setProductID(rs.getString("productID"));
                img.setImageUrl(rs.getString("imageUrl"));
                img.setIsMain(rs.getBoolean("isMain"));
                return img;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // có thể null nếu sp chưa có ảnh chính
    }

    // -----------------------------------------------------------
    // 3. Thêm ảnh mới cho sản phẩm
    //    - isMain có thể true hoặc false
    //    - ImageID là do bạn tự sinh (UUID) hoặc AUTO INCREMENT tùy DB
    // -----------------------------------------------------------
    public boolean insert(ProductImageDTO img) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "INSERT INTO ProductImage(imageID, productID, imageUrl, isMain) "
                       + "VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, img.getImageID());
            pst.setString(2, img.getProductID());
            pst.setString(3, img.getImageUrl());
            pst.setBoolean(4, img.isIsMain());

            int rows = pst.executeUpdate();
            return rows > 0; // thành công nếu có ít nhất 1 dòng thêm vào
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -----------------------------------------------------------
    // 4. Đặt 1 ảnh làm ảnh chính (thumbnail)
    //
    // Quy tắc web bán hàng phổ biến:
    //  - Mỗi product chỉ nên có 1 ảnh chính (isMain = 1)
    //  - Nên unset ảnh chính cũ trước, rồi set ảnh mới
    //
    // Mình tách làm 2 hàm để bạn dễ control transaction:
    //   a) clearMainImage(productID)
    //   b) setMainImage(imageID)
    // Bạn có thể gọi 2 hàm này lần lượt trong Controller.
    // -----------------------------------------------------------

    // a) clearMainImage: set isMain = 0 cho tất cả ảnh của product
    public boolean clearMainImage(String productID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE ProductImage SET isMain = 0 WHERE productID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productID);

            int rows = pst.executeUpdate();
            // rows có thể = 0 nếu sp chưa có ảnh nào, không phải bug
            return rows >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // b) setMainImage: set isMain = 1 cho ảnh cụ thể
    public boolean setMainImage(String imageID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE ProductImage SET isMain = 1 WHERE imageID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, imageID);

            int rows = pst.executeUpdate();
            return rows > 0; // ít nhất phải có 1 ảnh được set main
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -----------------------------------------------------------
    // 5. Xoá 1 ảnh cụ thể (vd: admin xóa ảnh phụ bị lỗi)
    // -----------------------------------------------------------
    public boolean delete(String imageID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM ProductImage WHERE imageID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, imageID);

            int rows = pst.executeUpdate();
            return rows > 0; // true nếu xóa được ít nhất 1 dòng
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -----------------------------------------------------------
    // 6. Xoá toàn bộ ảnh của một sản phẩm
    //    - Gọi khi admin xóa sản phẩm khỏi hệ thống để tránh ảnh mồ côi
    // -----------------------------------------------------------
    public boolean deleteByProductID(String productID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM ProductImage WHERE productID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, productID);

            int rows = pst.executeUpdate();
            // rows có thể >1 vì 1 sp có nhiều ảnh
            return rows >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -----------------------------------------------------------
    // 7. Cập nhật URL ảnh (trong trường hợp bạn đổi CDN / đổi đường dẫn)
    //    - Thường dùng trong phần admin: replace ảnh
    // -----------------------------------------------------------
    public boolean updateImageUrl(String imageID, String newUrl) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE ProductImage SET imageUrl = ? WHERE imageID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newUrl);
            pst.setString(2, imageID);

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
