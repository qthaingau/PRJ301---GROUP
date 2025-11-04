package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class PaymentDAO {

    public PaymentDAO() {
    }

    // 🟢 Lấy tất cả payment
    public ArrayList<PaymentDTO> getAllPayment() {
        ArrayList<PaymentDTO> listPayment = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Payment";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                PaymentDTO payment = new PaymentDTO();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setPaymentMethod(rs.getString("method"));
                payment.setPaymentStatus(rs.getString("status"));
                payment.setPaymentDate(rs.getDate("createdAt").toLocalDate());
                listPayment.add(payment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPayment;
    }

    // 🟢 Lấy payment theo ID
    public PaymentDTO getPaymentByID(String paymentID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Payment WHERE paymentID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, paymentID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                PaymentDTO payment = new PaymentDTO();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setPaymentMethod(rs.getString("method"));
                payment.setPaymentStatus(rs.getString("status"));
                payment.setPaymentDate(rs.getDate("createdAt").toLocalDate());
                return payment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔍 Search payment theo method hoặc status
    public List<PaymentDTO> searchPayment(String keyword) {
        List<PaymentDTO> listPayment = new ArrayList<>();
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "SELECT * FROM Payment WHERE method LIKE ? OR status LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                PaymentDTO payment = new PaymentDTO();
                payment.setPaymentID(rs.getString("paymentID"));
                payment.setPaymentMethod(rs.getString("method"));
                payment.setPaymentStatus(rs.getString("status"));
                payment.setPaymentDate(rs.getDate("createdAt").toLocalDate());
                listPayment.add(payment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPayment;
    }

    //  Thêm payment mới
    public boolean insert(PaymentDTO payment) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "INSERT INTO Payment(paymentID, method, status, createdAt) VALUES (?, ?, ?, GETDATE())";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, payment.getPaymentID());
            pst.setString(2, payment.getPaymentMethod());
            pst.setString(3, payment.getPaymentStatus());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✏️ Cập nhật payment
    public boolean update(PaymentDTO payment) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "UPDATE Payment SET method = ?, status = ? WHERE paymentID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, payment.getPaymentMethod());
            pst.setString(2, payment.getPaymentStatus());
            pst.setString(3, payment.getPaymentID());

            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ❌ Xóa payment
    public boolean delete(String paymentID) {
        try {
            Connection conn = DBUtils.getConnection();
            String sql = "DELETE FROM Payment WHERE paymentID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, paymentID);
            int i = pst.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
