/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.CategoryDAO;
import models.CategoryDTO;
import models.ProductDAO;
import models.ProductDTO;
import models.UserDTO;

/**
 *
 * @author TEST
 */
@WebServlet(name = "CategoryController", urlPatterns = {"/CategoryController"})
public class CategoryController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processSaveCategory(HttpServletRequest request, HttpServletResponse response, boolean isUpdate)
            throws ServletException, IOException {

        // Cờ phân biệt add / update
//        boolean isUpdate = Boolean.parseBoolean(request.getParameter("update"));
        // Lấy dữ liệu từ form
        String categoryID = request.getParameter("txtCategoryID");
        String categoryName = request.getParameter("txtCategoryName");
        String sportType = request.getParameter("txtSportType");
        String isActiveParam = request.getParameter("txtIsActive"); // chỉ có khi UPDATE + admin

        // Chuẩn hoá ID: Cxxx
        if (categoryID != null) {
            categoryID = categoryID.trim().toUpperCase();
        }

        boolean valid = true;

        // ==== VALIDATION CHUNG ====
        if (categoryID == null || !categoryID.matches("C\\d{3}")) {
            request.setAttribute("error_categoryID", "Category ID must follow format C***, e.g., C001");
            valid = false;
        }
        if (categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("error_categoryName", "Category name cannot be empty.");
            valid = false;
        }
        if (sportType == null || sportType.trim().isEmpty()) {
            request.setAttribute("error_sportType", "Sport type cannot be empty.");
            valid = false;
        }

        CategoryDAO dao = new CategoryDAO();

        // Lấy user để check quyền
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        boolean isAdmin = (user != null && "admin".equalsIgnoreCase(user.getRole()));

        // Xử lý isActive
        boolean isActive = true; // mặc định khi ADD

        if (isUpdate) {
            if (isAdmin) {
                // Admin update: đọc từ radio txtIsActive (true/false)
                if (isActiveParam != null) {
                    isActive = Boolean.parseBoolean(isActiveParam);
                }
            } else {
                // User thường: không được đổi trạng thái -> lấy từ DB
                CategoryDTO old = dao.getCategoryByID(categoryID);
                if (old != null) {
                    isActive = old.getIsActive();
                }
            }
        } else {
            // ADD: cho là active (hoặc dùng default trong DB)
            isActive = true;
        }

        // Nếu dữ liệu không hợp lệ -> quay lại form
        if (!valid) {
            CategoryDTO c = new CategoryDTO();
            c.setCategoryID(categoryID);
            c.setCategoryName(categoryName);
            c.setSportType(sportType);
            c.setIsActive(isActive);

            request.setAttribute("c", c);
            request.setAttribute("update", isUpdate);
            request.getRequestDispatcher("/admin/categoryForm.jsp").forward(request, response);
            return;
        }

        // Tạo DTO
        CategoryDTO category = new CategoryDTO(categoryID, categoryName, sportType, isActive);

        boolean success;
        if (isUpdate) {
            success = dao.update(category);
        } else {
            success = dao.insert(category);
        }

        if (success) {
            response.sendRedirect("MainController?txtAction=viewCategoryList");
        } else {
            request.setAttribute("error",
                    isUpdate ? "Failed to update category!" : "Failed to add new category! ID or name may already exist.");
            request.setAttribute("c", category);
            request.setAttribute("update", isUpdate);
            request.getRequestDispatcher("/admin/categoryForm.jsp").forward(request, response);
        }
    }

    private void processViewCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> listCategories = categoryDAO.getAllCategory();

        HttpSession session = request.getSession();
        session.setAttribute("listCategories", listCategories);
        request.getRequestDispatcher("customer/categoryList.jsp").forward(request, response);
    }

    private void processCallCategoryForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String updateParam = request.getParameter("update");
        boolean isUpdate = Boolean.parseBoolean(updateParam);

        String categoryID = request.getParameter("categoryID");
        CategoryDAO categoryDAO = new CategoryDAO();

        if (isUpdate && categoryID != null && !categoryID.isEmpty()) {
            CategoryDTO category = categoryDAO.getCategoryByID(categoryID);
            request.setAttribute("c", category);
        }

        request.setAttribute("update", isUpdate);
        request.getRequestDispatcher("admin/categoryForm.jsp").forward(request, response);
    }

    private void processFilterCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> list;   // dùng List, không dùng ArrayList

        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                // Không nhập gì -> lấy tất cả
                list = categoryDAO.getAllCategory();
            } else {
                // Có keyword -> filter
                list = categoryDAO.filterCategory(keyword);
            }

            // Gửi dữ liệu cho JSP
            request.setAttribute("listCategories", list);
            request.setAttribute("keyword", keyword);

            // Forward tới trang categoryList.jsp
            request.getRequestDispatcher("customer/categoryList.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error searching category!");
            request.getRequestDispatcher("customer/categoryList.jsp")
                    .forward(request, response);
        }
    }

    private void processDeleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryID = request.getParameter("categoryID");

        try {
            if (categoryID != null && !categoryID.trim().isEmpty()) {
                categoryID = categoryID.trim().toUpperCase();   // phòng khi id bị thường

                CategoryDAO dao = new CategoryDAO();
                boolean success = dao.delete(categoryID);

                if (!success) {
                    request.setAttribute("msg",
                            "Failed to delete category (ID: " + categoryID + ")!");
                }
            } else {
                request.setAttribute("msg", "Category ID is missing, cannot delete!");
            }

            // Cách 1: Gọi lại hàm view để luôn có listCategories đúng
            processViewCategoryList(request, response);

            // Cách 2 (nếu thích redirect qua MainController):
            // response.sendRedirect("MainController?txtAction=viewCategoryList");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error while deleting category!");
            processViewCategoryList(request, response);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");

        if (txtAction.equals("viewCategoryList")) {
            processViewCategoryList(request, response);
        } else if (txtAction.equals("callBrandForm")) {
            processCallCategoryForm(request, response);
        } else if (txtAction.equals("filterCategory")) {
            processFilterCategory(request, response);
        } else if (txtAction.equals("addCategory")) {
            processSaveCategory(request, response, false);
        } else if (txtAction.equals("updateCategory")) {
            processSaveCategory(request, response, true);
        } else if (txtAction.equals("deleteCategory")) {
            processDeleteCategory(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
