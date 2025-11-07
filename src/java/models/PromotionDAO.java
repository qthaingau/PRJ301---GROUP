//package models;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import utils.DBUtils;
//
///**
// * DAO thao tác với bảng Promotion.
// */
//public class PromotionDAO {
//
//	public List<PromotionDTO> getAllPromotions() {
//		List<PromotionDTO> promotions = new ArrayList<>();
//		String sql = "SELECT discountID, discountName, description, discountPercent, startDate, endDate, isActive FROM Promotion ORDER BY startDate DESC";
//		try (Connection conn = DBUtils.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
//			while (rs.next()) {
//				promotions.add(mapRow(rs));
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return promotions;
//	}
//
//	public PromotionDTO getPromotionById(String discountID) {
//		String sql = "SELECT discountID, discountName, description, discountPercent, startDate, endDate, isActive FROM Promotion WHERE discountID = ?";
//		try (Connection conn = DBUtils.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
//			pst.setString(1, discountID);
//			try (ResultSet rs = pst.executeQuery()) {
//				if (rs.next()) {
//					return mapRow(rs);
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return null;
//	}
//
//	public boolean insertPromotion(PromotionDTO dto) {
//		String sql = "INSERT INTO Promotion(discountID, discountName, description, discountPercent, startDate, endDate, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
//		try (Connection conn = DBUtils.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
//			pst.setString(1, dto.getDiscountID());
//			pst.setString(2, dto.getDiscountName());
//			pst.setString(3, dto.getDescription());
//			pst.setDouble(4, dto.getDiscountPercent());
//			pst.setDate(5, dto.getStartDate() != null ? Date.valueOf(dto.getStartDate()) : null);
//			pst.setDate(6, dto.getEndDate() != null ? Date.valueOf(dto.getEndDate()) : null);
//			pst.setBoolean(7, dto.isActive());
//			return pst.executeUpdate() > 0;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return false;
//	}
//
//	public boolean updatePromotion(PromotionDTO dto) {
//		String sql = "UPDATE Promotion SET discountName = ?, description = ?, discountPercent = ?, startDate = ?, endDate = ?, isActive = ? WHERE discountID = ?";
//		try (Connection conn = DBUtils.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
//			pst.setString(1, dto.getDiscountName());
//			pst.setString(2, dto.getDescription());
//			pst.setDouble(3, dto.getDiscountPercent());
//			pst.setDate(4, dto.getStartDate() != null ? Date.valueOf(dto.getStartDate()) : null);
//			pst.setDate(5, dto.getEndDate() != null ? Date.valueOf(dto.getEndDate()) : null);
//			pst.setBoolean(6, dto.isActive());
//			pst.setString(7, dto.getDiscountID());
//			return pst.executeUpdate() > 0;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return false;
//	}
//
//	public boolean deletePromotion(String discountID) {
//		String sql = "DELETE FROM Promotion WHERE discountID = ?";
//		try (Connection conn = DBUtils.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
//			pst.setString(1, discountID);
//			return pst.executeUpdate() > 0;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return false;
//	}
//
//	private PromotionDTO mapRow(ResultSet rs) throws SQLException {
//		PromotionDTO dto = new PromotionDTO();
//		dto.setDiscountID(rs.getString("discountID"));
//		dto.setDiscountName(rs.getString("discountName"));
//		dto.setDescription(rs.getString("description"));
//		dto.setDiscountPercent(rs.getDouble("discountPercent"));
//		Date start = rs.getDate("startDate");
//		Date end = rs.getDate("endDate");
//		dto.setStartDate(start != null ? start.toLocalDate() : null);
//		dto.setEndDate(end != null ? end.toLocalDate() : null);
//		dto.setIsActive(rs.getBoolean("isActive"));
//		return dto;
//	}
//}
