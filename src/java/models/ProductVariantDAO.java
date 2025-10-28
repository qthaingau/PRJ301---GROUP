package models; // Khai báo package chứa class này

// Import các thư viện cần thiết để thao tác với cơ sở dữ liệu và danh sách
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils; // Class tiện ích để lấy kết nối tới DB

// Lớp DAO (Data Access Object) để thao tác dữ liệu bảng ProductVariant
public class ProductVariantDAO {

    // Constructor mặc định
    public ProductVariantDAO() {
    }

    // ---------------------- LẤY TẤT CẢ PRODUCT VARIANT ----------------------
    public ArrayList<ProductVariantDTO> getAllVariants() {
        ArrayList<ProductVariantDTO> listVariant = new ArrayList<>(); // Tạo danh sách rỗng để chứa kết quả
        try {
            Connection conn = DBUtils.getConnection(); // Kết nối database
            String sql = "SELECT * FROM ProductVariant"; // Câu truy vấn lấy tất cả dữ liệu
            PreparedStatement pst = conn.prepareStatement(sql); // Chuẩn bị câu lệnh SQL
            ResultSet rs = pst.executeQuery(); // Thực thi và trả về kết quả

            // Duyệt qua từng dòng dữ liệu trong ResultSet
            while (rs.next()) {
                ProductVariantDTO variant = new ProductVariantDTO(); // Tạo đối tượng ProductVariantDTO
                variant.setVariantID(rs.getString("variantID")); // Lấy giá trị cột variantID
                variant.setProductID(rs.getString("productID")); // Lấy giá trị cột productID
                variant.setSize(rs.getString("size")); // Lấy giá trị cột size
                variant.setColor(rs.getString("color")); // Lấy giá trị cột color
                variant.setStock(rs.getInt("stock")); // Lấy giá trị cột stock
                variant.setPrice(rs.getDouble("price")); // Lấy giá trị cột price
                variant.setSalesCount(rs.getInt("salesCount")); // Lấy giá trị cột salesCount
                listVariant.add(variant); // Thêm vào danh sách
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console nếu có
        }
        return listVariant; // Trả về danh sách variants
    }

    // ---------------------- LẤY VARIANT THEO ID ----------------------
    public ProductVariantDTO getVariantByID(String variantID) {
        try {
            Connection conn = DBUtils.getConnection(); // Kết nối DB
            String sql = "SELECT * FROM ProductVariant WHERE variantID = ?"; // Câu SQL với điều kiện theo ID
            PreparedStatement pst = conn.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, variantID); // Gán giá trị cho dấu hỏi đầu tiên

            ResultSet rs = pst.executeQuery(); // Thực thi truy vấn

            // Nếu có kết quả trả về
            if (rs.next()) {
                ProductVariantDTO variant = new ProductVariantDTO(); // Tạo đối tượng DTO
                variant.setVariantID(rs.getString("variantID"));
                variant.setProductID(rs.getString("productID"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStock(rs.getInt("stock"));
                variant.setPrice(rs.getDouble("price"));
                variant.setSalesCount(rs.getInt("salesCount"));
                return variant; // Trả về đối tượng tìm được
            }
        } catch (Exception e) {
            e.printStackTrace(); // Bắt và in lỗi
        }
        return null; // Không tìm thấy => trả null
    }

    // ---------------------- LẤY DANH SÁCH VARIANT THEO PRODUCT ID ----------------------
    public List<ProductVariantDTO> getVariantsByProductID(String productID) {
        List<ProductVariantDTO> listVariant = new ArrayList<>(); // Danh sách kết quả
        try {
            Connection conn = DBUtils.getConnection(); // Kết nối DB
            String sql = "SELECT * FROM ProductVariant WHERE productID = ?"; // Câu SQL có điều kiện productID
            PreparedStatement pst = conn.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, productID); // Gán giá trị cho tham số
            ResultSet rs = pst.executeQuery(); // Thực thi truy vấn

            // Duyệt qua các dòng kết quả
            while (rs.next()) {
                ProductVariantDTO variant = new ProductVariantDTO(); // Tạo đối tượng DTO mới
                variant.setVariantID(rs.getString("variantID"));
                variant.setProductID(rs.getString("productID"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStock(rs.getInt("stock"));
                variant.setPrice(rs.getDouble("price"));
                variant.setSalesCount(rs.getInt("salesCount"));
                listVariant.add(variant); // Thêm vào danh sách kết quả
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return listVariant; // Trả về danh sách variant
    }

    // ---------------------- THÊM MỚI VARIANT ----------------------
    public boolean insert(ProductVariantDTO variant) {
        try {
            Connection c = DBUtils.getConnection(); // Kết nối DB
            String sql = "INSERT INTO ProductVariant(variantID, productID, size, color, stock, price, salesCount) "
                       + "VALUES(?, ?, ?, ?, ?, ?, ?)"; // Câu SQL thêm dữ liệu mới

            PreparedStatement pst = c.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, variant.getVariantID()); // Gán giá trị cho variantID
            pst.setString(2, variant.getProductID()); // Gán giá trị cho productID
            pst.setString(3, variant.getSize()); // Gán giá trị cho size
            pst.setString(4, variant.getColor()); // Gán giá trị cho color
            pst.setInt(5, variant.getStock()); // Gán giá trị cho stock
            pst.setDouble(6, variant.getPrice()); // Gán giá trị cho price
            pst.setInt(7, variant.getSalesCount()); // Gán giá trị cho salesCount

            int rows = pst.executeUpdate(); // Thực thi câu lệnh INSERT
            return rows > 0; // Trả về true nếu có ít nhất 1 dòng bị ảnh hưởng
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return false; // Nếu lỗi => trả false
    }

    // ---------------------- CẬP NHẬT VARIANT ----------------------
    public boolean update(ProductVariantDTO variant) {
        try {
            Connection c = DBUtils.getConnection(); // Kết nối DB
            String sql = "UPDATE ProductVariant "
                       + "SET productID = ?, " // Cập nhật productID
                       + "size = ?, " // Cập nhật size
                       + "color = ?, " // Cập nhật color
                       + "stock = ?, " // Cập nhật stock
                       + "price = ?, " // Cập nhật price
                       + "salesCount = ? " // Cập nhật salesCount
                       + "WHERE variantID = ?"; // Điều kiện WHERE theo variantID

            PreparedStatement pst = c.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, variant.getProductID());
            pst.setString(2, variant.getSize());
            pst.setString(3, variant.getColor());
            pst.setInt(4, variant.getStock());
            pst.setDouble(5, variant.getPrice());
            pst.setInt(6, variant.getSalesCount());
            pst.setString(7, variant.getVariantID());

            int i = pst.executeUpdate(); // Thực thi câu lệnh UPDATE
            return i > 0; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi
        }
        return false; // Nếu có lỗi => false
    }

    // ---------------------- XÓA VARIANT ----------------------
    public boolean delete(String variantID) {
        try {
            Connection c = DBUtils.getConnection(); // Kết nối DB
            String sql = "DELETE FROM ProductVariant WHERE variantID = ?"; // Câu SQL xóa dữ liệu theo variantID

            PreparedStatement pst = c.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, variantID); // Gán giá trị variantID

            int i = pst.executeUpdate(); // Thực thi câu lệnh DELETE
            return i > 0; // Nếu xóa thành công => true
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi
        }
        return false; // Nếu lỗi => false
    }
}
