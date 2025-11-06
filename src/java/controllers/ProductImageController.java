package controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.ProductImageDAO;
import models.ProductImageDTO;

/**
 * Servlet quản lý các thao tác CRUD cho ảnh sản phẩm.
 */
@WebServlet(name = "ProductImageController", urlPatterns = {"/ProductImageController"})
public class ProductImageController extends HttpServlet {

	private static final String ERROR_PAGE = "error.jsp";
	private static final String PRODUCT_IMAGE_PAGE = "admin/productImages.jsp";
	private static final String ATTR_ERROR = "ERROR_MESSAGE";
	private static final String ATTR_SUCCESS = "SUCCESS_MESSAGE";
	private static final String ATTR_PRODUCT_ID = "PRODUCT_ID";
	private static final String ATTR_IMAGES_LIST = "IMAGES_LIST";
	private static final String ATTR_MAIN_IMAGE = "MAIN_IMAGE";
	private static final String PARAM_TXT_ACTION = "txtAction";
	private static final String PARAM_PRODUCT_ID = "productID";
	private static final String PARAM_IMAGE_URL = "imageUrl";
	private static final String PARAM_IS_MAIN = "isMain";
	private static final String PARAM_IMAGE_ID = "imageID";
	private static final String PARAM_NEW_URL = "newUrl";

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

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

	String action = request.getParameter(PARAM_TXT_ACTION);
		if (action == null || action.trim().isEmpty()) {
			action = "viewImages";
		}

		String destination;
		try {
			switch (action) {
				case "viewImages":
					destination = viewImages(request);
					break;
				case "addImage":
					destination = addImage(request);
					break;
				case "deleteImage":
					destination = deleteImage(request);
					break;
				case "setMainImage":
					destination = setMainImage(request);
					break;
				case "updateImageUrl":
					destination = updateImageUrl(request);
					break;
				case "deleteAllImages":
					destination = deleteAllImages(request);
					break;
				default:
					request.setAttribute(ATTR_ERROR, "Không tìm thấy hành động phù hợp.");
					destination = ERROR_PAGE;
			}
		} catch (Exception ex) {
			log("Error at ProductImageController", ex);
			request.setAttribute(ATTR_ERROR, "Đã xảy ra lỗi: " + ex.getMessage());
			destination = ERROR_PAGE;
		}

		request.getRequestDispatcher(destination).forward(request, response);
	}

	private String viewImages(HttpServletRequest request) {
		String productID = request.getParameter(PARAM_PRODUCT_ID);
		if (isBlank(productID)) {
			request.setAttribute(ATTR_ERROR, "Thiếu thông tin sản phẩm.");
			return ERROR_PAGE;
		}

		populateProductImages(request, productID.trim());
		return PRODUCT_IMAGE_PAGE;
	}

	private String addImage(HttpServletRequest request) {
		String productID = request.getParameter(PARAM_PRODUCT_ID);
		String imageUrl = request.getParameter(PARAM_IMAGE_URL);
		boolean isMain = parseBoolean(request.getParameter(PARAM_IS_MAIN));

		if (isBlank(productID) || isBlank(imageUrl)) {
			request.setAttribute(ATTR_ERROR, "Vui lòng cung cấp đầy đủ Product ID và URL ảnh.");
			if (!isBlank(productID)) {
				populateProductImages(request, productID.trim());
				return PRODUCT_IMAGE_PAGE;
			}
			return ERROR_PAGE;
		}

		ProductImageDAO dao = new ProductImageDAO();
		String normalizedProductID = productID.trim();
		String normalizedUrl = imageUrl.trim();
		ProductImageDTO dto = new ProductImageDTO(UUID.randomUUID().toString(), normalizedProductID, normalizedUrl, isMain);

		if (isMain) {
			dao.clearMainImage(normalizedProductID);
		}

		boolean success = dao.insert(dto);
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Thêm ảnh thành công.");
		} else {
			request.setAttribute(ATTR_ERROR, "Thêm ảnh thất bại.");
		}

		populateProductImages(request, normalizedProductID);
		return PRODUCT_IMAGE_PAGE;
	}

	private String deleteImage(HttpServletRequest request) {
		String imageID = request.getParameter(PARAM_IMAGE_ID);
		String productID = request.getParameter(PARAM_PRODUCT_ID);

		if (isBlank(imageID) || isBlank(productID)) {
			request.setAttribute(ATTR_ERROR, "Thiếu thông tin ảnh hoặc sản phẩm.");
			return ERROR_PAGE;
		}

		ProductImageDAO dao = new ProductImageDAO();
		boolean success = dao.delete(imageID.trim());
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Xóa ảnh thành công.");
		} else {
			request.setAttribute(ATTR_ERROR, "Xóa ảnh thất bại.");
		}

		populateProductImages(request, productID.trim());
		return PRODUCT_IMAGE_PAGE;
	}

	private String setMainImage(HttpServletRequest request) {
		String imageID = request.getParameter(PARAM_IMAGE_ID);
		String productID = request.getParameter(PARAM_PRODUCT_ID);

		if (isBlank(imageID) || isBlank(productID)) {
			request.setAttribute(ATTR_ERROR, "Thiếu thông tin ảnh hoặc sản phẩm.");
			return ERROR_PAGE;
		}

		ProductImageDAO dao = new ProductImageDAO();
		String pid = productID.trim();
		dao.clearMainImage(pid);
		boolean success = dao.setMainImage(imageID.trim());
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Đặt ảnh chính thành công.");
		} else {
			request.setAttribute(ATTR_ERROR, "Không thể đặt ảnh chính.");
		}

		populateProductImages(request, pid);
		return PRODUCT_IMAGE_PAGE;
	}

	private String updateImageUrl(HttpServletRequest request) {
		String imageID = request.getParameter(PARAM_IMAGE_ID);
		String productID = request.getParameter(PARAM_PRODUCT_ID);
		String newUrl = request.getParameter(PARAM_NEW_URL);

		if (isBlank(imageID) || isBlank(productID) || isBlank(newUrl)) {
			request.setAttribute(ATTR_ERROR, "Thiếu thông tin cần thiết để cập nhật ảnh.");
			if (!isBlank(productID)) {
				populateProductImages(request, productID.trim());
				return PRODUCT_IMAGE_PAGE;
			}
			return ERROR_PAGE;
		}

		ProductImageDAO dao = new ProductImageDAO();
		boolean success = dao.updateImageUrl(imageID.trim(), newUrl.trim());
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Cập nhật URL ảnh thành công.");
		} else {
			request.setAttribute(ATTR_ERROR, "Cập nhật URL ảnh thất bại.");
		}

		populateProductImages(request, productID.trim());
		return PRODUCT_IMAGE_PAGE;
	}

	private String deleteAllImages(HttpServletRequest request) {
		String productID = request.getParameter(PARAM_PRODUCT_ID);
		if (isBlank(productID)) {
			request.setAttribute(ATTR_ERROR, "Thiếu Product ID để xóa ảnh.");
			return ERROR_PAGE;
		}

		ProductImageDAO dao = new ProductImageDAO();
		boolean success = dao.deleteByProductID(productID.trim());
		if (success) {
			request.setAttribute(ATTR_SUCCESS, "Đã xóa mọi ảnh của sản phẩm.");
		} else {
			request.setAttribute(ATTR_ERROR, "Không thể xóa ảnh của sản phẩm.");
		}

		populateProductImages(request, productID.trim());
		return PRODUCT_IMAGE_PAGE;
	}

	private void populateProductImages(HttpServletRequest request, String productID) {
		ProductImageDAO dao = new ProductImageDAO();
		List<ProductImageDTO> images = dao.getImagesByProductID(productID);
		ProductImageDTO mainImage = dao.getMainImageByProductID(productID);

	request.setAttribute(ATTR_PRODUCT_ID, productID);
	request.setAttribute(ATTR_IMAGES_LIST, images);
	request.setAttribute(ATTR_MAIN_IMAGE, mainImage);
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean parseBoolean(String value) {
		if (value == null) {
			return false;
		}
		return "true".equalsIgnoreCase(value)
				|| "on".equalsIgnoreCase(value)
				|| "1".equals(value);
	}
}
