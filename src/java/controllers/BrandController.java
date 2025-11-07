package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.BrandDAO;
import models.BrandDTO;
import models.UserDTO;

@WebServlet(name = "BrandController", urlPatterns = {"/BrandController"})
public class BrandController extends HttpServlet {

    // Hiển thị tất cả brand (quản lý)
    private void processViewBrandList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            BrandDAO dao = new BrandDAO();
            List<BrandDTO> listBrands = dao.getAllBrand();
            request.setAttribute("brandList", listBrands);
            request.getRequestDispatcher("customer/brandList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error loading brand list!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Lọc brand theo keyword
    private void processFilterBrand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            BrandDAO dao = new BrandDAO();
            List<BrandDTO> list;

            if (keyword == null || keyword.trim().isEmpty()) {
                list = dao.getAllBrand();
            } else {
                list = dao.filterBrand(keyword);
            }

            request.setAttribute("brandList", list);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("customer/brandList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error filtering brand!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // GỖP ADD + UPDATE BRAND
    private void processSaveBrand(HttpServletRequest request, HttpServletResponse response, boolean isUpdate)
            throws ServletException, IOException {

        // Cờ phân biệt add / update
//        boolean isUpdate = Boolean.parseBoolean(request.getParameter("update"));
        // Lấy dữ liệu từ form
        String brandID = request.getParameter("txtBrandID");
        String brandName = request.getParameter("txtBrandName");
        String origin = request.getParameter("txtOrigin");
        String isActiveParam = request.getParameter("txtIsActive"); // radio "true"/"false" (chỉ có khi update + admin)

        // Chuẩn hoá
        if (brandID != null) {
            brandID = brandID.trim().toUpperCase(); // b001 -> B001
        }
        if (brandName != null) {
            brandName = brandName.trim();
        }
        if (origin != null) {
            origin = origin.trim();
        }

        boolean valid = true;

        // ===== VALIDATE CHUNG =====
        if (brandID == null || !brandID.matches("B\\d{3}")) {
            request.setAttribute("error_brandID", "Brand ID must follow format B***, e.g., B001");
            valid = false;
        }
        if (brandName == null || brandName.isEmpty()) {
            request.setAttribute("error_brandName", "Brand name cannot be empty.");
            valid = false;
        }
        if (origin == null || origin.isEmpty()) {
            request.setAttribute("error_origin", "Origin cannot be empty.");
            valid = false;
        }

        BrandDAO dao = new BrandDAO();

        // Lấy user để check quyền
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        boolean isAdmin = (user != null && "admin".equalsIgnoreCase(user.getRole()));

        // XỬ LÝ isActive
        boolean isActive = true; // mặc định khi ADD

        if (isUpdate) {
            if (isAdmin) {
                // Admin update: đọc từ radio txtIsActive
                if (isActiveParam != null) {
                    isActive = Boolean.parseBoolean(isActiveParam);
                }
            } else {
                // User thường: không cho đổi status -> lấy từ DB
                BrandDTO old = dao.getBrandByID(brandID);
                if (old != null) {
                    isActive = old.isIsActive();
                }
            }
        } else {
            // ADD: để true (hoặc dùng default trong DB)
            isActive = true;
        }

        // Nếu dữ liệu không hợp lệ -> quay lại form
        if (!valid) {
            BrandDTO b = new BrandDTO();
            b.setBrandID(brandID);
            b.setBrandName(brandName);
            b.setOrigin(origin);
            b.setIsActive(isActive);

            request.setAttribute("b", b);
            request.setAttribute("update", isUpdate);
            request.getRequestDispatcher("/admin/brandForm.jsp").forward(request, response);
            return;
        }

        // Tạo DTO để lưu
        BrandDTO brand = new BrandDTO(brandID, brandName, origin, isActive);

        boolean success;
        if (isUpdate) {
            success = dao.update(brand);
        } else {
            success = dao.insert(brand);
        }

        if (success) {
            // Thành công -> quay lại Brand List
            response.sendRedirect("MainController?txtAction=viewBrandList");
        } else {
            // Thất bại -> báo lỗi + giữ lại dữ liệu
            request.setAttribute("error",
                    isUpdate ? "Failed to update brand!"
                            : "Failed to add new brand! ID or name may already exist.");
            request.setAttribute("b", brand);
            request.setAttribute("update", isUpdate);
            request.getRequestDispatcher("/admin/brandForm.jsp").forward(request, response);
        }
    }

    private void processDeleteBrand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String brandID = request.getParameter("brandID");

        try {
            if (brandID != null && !brandID.trim().isEmpty()) {
                brandID = brandID.trim().toUpperCase();

                BrandDAO dao = new BrandDAO();
                boolean success = dao.delete(brandID);   // delete = set isActive = 0

                if (!success) {
                    request.setAttribute("msg",
                            "Failed to deactivate brand (ID: " + brandID + ")!");
                }
            } else {
                request.setAttribute("msg", "Brand ID is missing, cannot delete!");
            }

            // Sau khi xoá (deactivate) xong -> quay lại danh sách brand
            processViewBrandList(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error while deleting brand!");
            processViewBrandList(request, response);
        }
    }

    private void processCallBrandForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tham số update từ URL (true / false)
        String updateParam = request.getParameter("update");
        boolean isUpdate = Boolean.parseBoolean(updateParam);

        // Lấy brandID (nếu có)
        String brandID = request.getParameter("brandID");

        // Nếu là update -> lấy thông tin Brand từ DB để hiển thị lên form
        if (isUpdate && brandID != null && !brandID.trim().isEmpty()) {
            BrandDAO dao = new BrandDAO();
            BrandDTO brand = dao.getBrandByID(brandID.trim().toUpperCase());
            request.setAttribute("b", brand);
        }

        // Gửi flag update để JSP biết đang ở chế độ Add hay Update
        request.setAttribute("update", isUpdate);

        // Chuyển hướng đến trang form
        request.getRequestDispatcher("/admin/brandForm.jsp").forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");

        if (txtAction.equals("viewBrandList")) {
            processViewBrandList(request, response);
        } else if (txtAction.equals("callBrandForm")) {
            processCallBrandForm(request, response);
        } else if (txtAction.equals("filterBrand")) {
            processFilterBrand(request, response);
        } else if ("updateBrand".equals(txtAction)) {
            processSaveBrand(request, response, true);
        } else if ("addBrand".equals(txtAction)) {
            processSaveBrand(request, response, false);
        } else if ("deleteBrand".equals(txtAction)) {
            processDeleteBrand(request, response);
        }

    }

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

    @Override
    public String getServletInfo() {
        return "BrandController - xử lý brand riêng";
    }
}
