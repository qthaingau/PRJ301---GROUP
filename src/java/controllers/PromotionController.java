package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.PromotionDAO;
import models.PromotionDTO;

/**
 * Servlet quản lý CRUD cho bảng Promotion.
 */
@WebServlet(name = "PromotionController", urlPatterns = {"/PromotionController"})
public class PromotionController extends HttpServlet {

	private static final String ERROR_PAGE = "error.jsp";
	private static final String LIST_PAGE = "admin/promotionList.jsp";
	private static final String FORM_PAGE = "admin/promotionForm.jsp";
	private static final String ATTR_ERROR = "ERROR_MESSAGE";
	private static final String ATTR_SUCCESS = "SUCCESS_MESSAGE";
	private static final String ATTR_PROMOTION_LIST = "PROMOTION_LIST";
	private static final String ATTR_PROMOTION = "PROMOTION";
	private static final String ATTR_FORM_ACTION = "FORM_ACTION";
	private static final String ATTR_FORM_TITLE = "FORM_TITLE";
	private static final String PARAM_TXT_ACTION = "txtAction";
	private static final String PARAM_DISCOUNT_ID = "discountID";
	private static final String PARAM_DISCOUNT_NAME = "discountName";
	private static final String PARAM_DESCRIPTION = "description";
	private static final String PARAM_DISCOUNT_PERCENT = "discountPercent";
	private static final String PARAM_START_DATE = "startDate";
	private static final String PARAM_END_DATE = "endDate";
	private static final String PARAM_IS_ACTIVE = "isActive";
	private static final String ACTION_VIEW = "viewPromotions";
	private static final String ACTION_CALL_ADD = "callAddPromotion";
	private static final String ACTION_ADD = "addPromotion";
	private static final String ACTION_CALL_UPDATE = "callUpdatePromotion";
	private static final String ACTION_UPDATE = "updatePromotion";
	private static final String ACTION_DELETE = "deletePromotion";
	private static final String TITLE_ADD = "Thêm khuyến mãi mới";
	private static final String TITLE_UPDATE = "Cập nhật khuyến mãi";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	// xác định hành động cần thực hiện rồi chuyển tiếp tới trang phù hợp
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String action = request.getParameter(PARAM_TXT_ACTION);
		if (action == null || action.trim().isEmpty()) {
			action = ACTION_VIEW;
		}

		String destination;
		try {
			switch (action) {
				case ACTION_VIEW:
					destination = viewPromotions(request);
					break;
				case ACTION_CALL_ADD:
					destination = showPromotionForm(request, new PromotionDTO(), ACTION_ADD, TITLE_ADD);
					break;
				case ACTION_ADD:
					destination = addPromotion(request);
					break;
				case ACTION_CALL_UPDATE:
					destination = prepareUpdatePromotion(request);
					break;
				case ACTION_UPDATE:
					destination = updatePromotion(request);
					break;
				case ACTION_DELETE:
					destination = deletePromotion(request);
					break;
				default:
					request.setAttribute(ATTR_ERROR, "Không tìm thấy hành động phù hợp.");
					destination = ERROR_PAGE;
			}
		} catch (Exception ex) {
			log("Error at PromotionController", ex);
			request.setAttribute(ATTR_ERROR, "Đã xảy ra lỗi: " + ex.getMessage());
			destination = ERROR_PAGE;
		}

		request.getRequestDispatcher(destination).forward(request, response);
	}

	//tải toàn bộ danh sách khuyến mãi và đẩy sang trang quản lý
	private String viewPromotions(HttpServletRequest request) throws Exception {
		PromotionDAO dao = new PromotionDAO();
	request.setAttribute(ATTR_PROMOTION_LIST, dao.getAllPromotions());
		return LIST_PAGE;
	}

	//dựng DTO, kiểm tra hợp lệ rồi ghi DB
	private String addPromotion(HttpServletRequest request) throws Exception {
		PromotionDTO dto = buildPromotionFromRequest(request);
		List<String> errors = validatePromotion(dto, true);
		if (!errors.isEmpty()) {
			request.setAttribute(ATTR_ERROR, String.join("<br/>", errors));
			return showPromotionForm(request, dto, ACTION_ADD, TITLE_ADD);
		}

		PromotionDAO dao = new PromotionDAO();
		boolean success = dao.insertPromotion(dto);
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Thêm khuyến mãi thành công.");
		} else {
			request.setAttribute(ATTR_ERROR, "Không thể thêm khuyến mãi.");
			return showPromotionForm(request, dto, ACTION_ADD, TITLE_ADD);
		}

		return viewPromotions(request);
	}

	//lấy dữ liệu khuyến mãi hiện tại để hiển thị lên form cập nhật
	private String prepareUpdatePromotion(HttpServletRequest request) throws Exception {
		String discountID = request.getParameter(PARAM_DISCOUNT_ID);
		if (isBlank(discountID)) {
			request.setAttribute(ATTR_ERROR, "Thiếu mã khuyến mãi.");
			return viewPromotions(request);
		}

		PromotionDAO dao = new PromotionDAO();
		PromotionDTO dto = dao.getPromotionById(discountID.trim());
		if (dto == null) {
			request.setAttribute(ATTR_ERROR, "Không tìm thấy khuyến mãi cần sửa.");
			return viewPromotions(request);
		}

		return showPromotionForm(request, dto, ACTION_UPDATE, TITLE_UPDATE);
	}

	//xử lý submit cập nhật: dựng DTO, validate và gọi DAO update.
	private String updatePromotion(HttpServletRequest request) throws Exception {
		PromotionDTO dto = buildPromotionFromRequest(request);
		List<String> errors = validatePromotion(dto, false);
		if (!errors.isEmpty()) {
			request.setAttribute(ATTR_ERROR, String.join("<br/>", errors));
			return showPromotionForm(request, dto, ACTION_UPDATE, TITLE_UPDATE);
		}

		PromotionDAO dao = new PromotionDAO();
		boolean success = dao.updatePromotion(dto);
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Cập nhật khuyến mãi thành công.");
		} else {
			request.setAttribute(ATTR_ERROR, "Không thể cập nhật khuyến mãi.");
			return showPromotionForm(request, dto, ACTION_UPDATE, TITLE_UPDATE);
		}

		return viewPromotions(request);
	}

	//xóa một khuyến mãi theo discountID, sau đó tải lại danh sách
	private String deletePromotion(HttpServletRequest request) throws Exception {
		String discountID = request.getParameter(PARAM_DISCOUNT_ID);
		if (isBlank(discountID)) {
			request.setAttribute(ATTR_ERROR, "Thiếu mã khuyến mãi để xóa.");
			return viewPromotions(request);
		}

		PromotionDAO dao = new PromotionDAO();
		boolean success = dao.deletePromotion(discountID.trim());
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Đã xóa khuyến mãi.");
		} else {
			request.setAttribute(ATTR_ERROR, "Không thể xóa khuyến mãi.");
		}

		return viewPromotions(request);
	}

	//chuẩn bị dữ liệu hiển thị form (thêm/sửa) với DTO và thông tin tiêu đề/hành động
	private String showPromotionForm(HttpServletRequest request, PromotionDTO dto, String action, String title) {
	request.setAttribute(ATTR_PROMOTION, dto);
	request.setAttribute(ATTR_FORM_ACTION, action);
	request.setAttribute(ATTR_FORM_TITLE, title);
		return FORM_PAGE;
	}

	//chuyển các tham số request thành PromotionDTO, parse kiểu dữ liệu phức tạp
	private PromotionDTO buildPromotionFromRequest(HttpServletRequest request) {
	String discountID = trimOrNull(request.getParameter(PARAM_DISCOUNT_ID));
	String name = trimOrNull(request.getParameter(PARAM_DISCOUNT_NAME));
	String description = trimOrNull(request.getParameter(PARAM_DESCRIPTION));
	String percentStr = trimOrNull(request.getParameter(PARAM_DISCOUNT_PERCENT));
	String startDateStr = trimOrNull(request.getParameter(PARAM_START_DATE));
	String endDateStr = trimOrNull(request.getParameter(PARAM_END_DATE));
	boolean isActive = parseBoolean(request.getParameter(PARAM_IS_ACTIVE));

		Double percent = null;
		if (percentStr != null) {
			try {
				percent = Double.parseDouble(percentStr);
			} catch (NumberFormatException ex) {
				// sẽ xử lý trong validatePromotion
			}
		}

		LocalDate startDate = parseDate(startDateStr);
		LocalDate endDate = parseDate(endDateStr);

		PromotionDTO dto = new PromotionDTO();
		dto.setDiscountID(discountID);
		dto.setDiscountName(name);
		dto.setDescription(description);
		dto.setDiscountPercent(percent != null ? percent : 0);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		dto.setIsActive(isActive);
		return dto;
	}

	//kiểm tra Promotion; trả về danh sách lỗi nếu có
	private List<String> validatePromotion(PromotionDTO dto, boolean isCreate) {
		List<String> errors = new ArrayList<>();
		if (isCreate && isBlank(dto.getDiscountID())) {
			errors.add("Vui lòng nhập mã khuyến mãi.");
		}
		if (isBlank(dto.getDiscountName())) {
			errors.add("Vui lòng nhập tên khuyến mãi.");
		}
		if (dto.getDiscountPercent() <= 0) {
			errors.add("Phần trăm giảm giá phải lớn hơn 0.");
		}
		if (dto.getStartDate() != null && dto.getEndDate() != null
				&& dto.getEndDate().isBefore(dto.getStartDate())) {
			errors.add("Ngày kết thúc phải sau ngày bắt đầu.");
		}
		return errors;
	}

	private LocalDate parseDate(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException ex) {
			return null;
		}
	}

	private boolean parseBoolean(String value) {
		if (value == null) {
			return false;
		}
		return "true".equalsIgnoreCase(value)
				|| "on".equalsIgnoreCase(value)
				|| "1".equals(value);
	}

	private String trimOrNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
