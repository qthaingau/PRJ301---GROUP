package models; // Đặt class trong package models

// Import các thư viện cần thiết để làm việc với DB
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils; // Lớp tiện ích để lấy kết nối DB

// Lớp DAO (Data Access Object) dành cho bảng Brand
public class BrandDAO {

    // Constructor mặc định
    public BrandDAO() {
    }

    // ---------------------- LẤY TẤT CẢ CÁC BRAND ----------------------
    public ArrayList<BrandDTO> getAllBrand() {
        ArrayList<BrandDTO> listBrand = new ArrayList<>(); // Danh sách chứa kết quả
        try {
            Connection conn = DBUtils.getConnection(); // Lấy kết nối tới DB
            String sql = "SELECT * FROM Brand"; // Câu truy vấn SQL
            PreparedStatement pst = conn.prepareStatement(sql); // Chuẩn bị câu lệnh SQL
            ResultSet rs = pst.executeQuery(); // Thực thi truy vấn

            // Duyệt qua từng dòng kết quả
            while (rs.next()) {
                BrandDTO brand = new BrandDTO(); // Tạo đối tượng DTO
                brand.setBrandID(rs.getString("brandID")); // Gán giá trị cho brandID
                brand.setBrandName(rs.getString("brandName")); // Gán giá trị cho brandName
                brand.setOrigin(rs.getString("origin")); // Gán giá trị cho origin
                listBrand.add(brand); // Thêm đối tượng vào danh sách
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return listBrand; // Trả về danh sách brand
    }

    // ---------------------- LẤY BRAND THEO ID ----------------------
    public BrandDTO getBrandByID(String brandID) {
        try {
            Connection conn = DBUtils.getConnection(); // Kết nối DB
            String sql = "SELECT * FROM Brand WHERE brandID = ?"; // Câu SQL có điều kiện
            PreparedStatement pst = conn.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, brandID); // Gán giá trị cho tham số 1

            ResultSet rs = pst.executeQuery(); // Thực thi câu lệnh

            // Nếu có kết quả trả về
            if (rs.next()) {
                BrandDTO brand = new BrandDTO(); // Tạo đối tượng DTO
                brand.setBrandID(rs.getString("brandID"));
                brand.setBrandName(rs.getString("brandName"));
                brand.setOrigin(rs.getString("origin"));
                return brand; // Trả về kết quả
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return null; // Nếu không tìm thấy => null
    }

    // ---------------------- TÌM BRAND THEO TÊN ----------------------
    public List<BrandDTO> getBrandByName(String brandName) {
        List<BrandDTO> listBrand = new ArrayList<>(); // Danh sách kết quả
        try {
            Connection conn = DBUtils.getConnection(); // Kết nối DB
            String sql = "SELECT * FROM Brand WHERE brandName LIKE ?"; // Truy vấn có điều kiện LIKE
            PreparedStatement pst = conn.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, "%" + brandName + "%"); // Dùng wildcard để tìm gần đúng

            ResultSet rs = pst.executeQuery(); // Thực thi truy vấn

            while (rs.next()) {
                BrandDTO brand = new BrandDTO();
                brand.setBrandID(rs.getString("brandID"));
                brand.setBrandName(rs.getString("brandName"));
                brand.setOrigin(rs.getString("origin"));
                listBrand.add(brand); // Thêm vào danh sách kết quả
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return listBrand; // Trả về danh sách brand tìm được
    }

    // ---------------------- THÊM BRAND MỚI ----------------------
    public boolean insert(BrandDTO brand) {
        try {
            Connection c = DBUtils.getConnection(); // Lấy kết nối DB
            String sql = "INSERT INTO Brand(brandID, brandName, origin) VALUES(?, ?, ?)"; // Câu SQL thêm dữ liệu

            PreparedStatement pst = c.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, brand.getBrandID()); // Gán giá trị cho brandID
            pst.setString(2, brand.getBrandName()); // Gán giá trị cho brandName
            pst.setString(3, brand.getOrigin()); // Gán giá trị cho origin

            int rows = pst.executeUpdate(); // Thực thi câu lệnh INSERT
            return rows > 0; // Trả về true nếu có ít nhất 1 dòng được thêm
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return false; // Nếu lỗi => false
    }

    // ---------------------- CẬP NHẬT BRAND ----------------------
    public boolean update(BrandDTO brand) {
        try {
            Connection c = DBUtils.getConnection(); // Lấy kết nối DB
            String sql = "UPDATE Brand "
                       + "SET brandName = ?, " // Cập nhật tên thương hiệu
                       + "origin = ? " // Cập nhật quốc gia xuất xứ
                       + "WHERE brandID = ?"; // Điều kiện WHERE theo brandID

            PreparedStatement pst = c.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, brand.getBrandName()); // Gán giá trị mới cho brandName
            pst.setString(2, brand.getOrigin()); // Gán giá trị mới cho origin
            pst.setString(3, brand.getBrandID()); // Gán brandID cho WHERE

            int i = pst.executeUpdate(); // Thực thi câu lệnh UPDATE
            return i > 0; // Trả về true nếu có dòng được cập nhật
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi
        }
        return false; // Nếu có lỗi => false
    }

    // ---------------------- XÓA BRAND ----------------------
    public boolean delete(String brandID) {
        try {
            Connection c = DBUtils.getConnection(); // Lấy kết nối DB
            String sql = "DELETE FROM Brand WHERE brandID = ?"; // Câu SQL xóa dữ liệu

            PreparedStatement pst = c.prepareStatement(sql); // Chuẩn bị câu lệnh
            pst.setString(1, brandID); // Gán brandID cần xóa

            int i = pst.executeUpdate(); // Thực thi DELETE
            return i > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return false; // Nếu lỗi => false
    }
}
