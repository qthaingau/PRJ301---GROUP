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
    // ---------------------- ADD CATEGORY ----------------------
    private void processAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryID = request.getParameter("txtCategoryID");
        String categoryName = request.getParameter("txtCategoryName");
        String sportType = request.getParameter("txtSportType");

        boolean valid = true;

        // Validate dữ liệu nhập
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

        if (!valid) {
            request.setAttribute("update", false);
            request.getRequestDispatcher("/admin/categoryForm.jsp").forward(request, response);
            return;
        }

        // Tạo DTO và lưu
        CategoryDTO category = new CategoryDTO(categoryID, categoryName, sportType, true);
        CategoryDAO dao = new CategoryDAO();

        boolean success = dao.insert(category);

        if (success) {
            response.sendRedirect("MainController?txtAction=viewCategoryList");
        } else {
            request.setAttribute("error", "Failed to add new category! ID or name may already exist.");
            request.setAttribute("update", false);
            request.getRequestDispatcher("/admin/categoryForm.jsp").forward(request, response);
        }
    }

// ---------------------- UPDATE CATEGORY ----------------------
    private void processUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryID = request.getParameter("txtCategoryID");
        String categoryName = request.getParameter("txtCategoryName");
        String sportType = request.getParameter("txtSportType");

        boolean valid = true;

        if (categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("error_categoryName", "Category name cannot be empty.");
            valid = false;
        }
        if (sportType == null || sportType.trim().isEmpty()) {
            request.setAttribute("error_sportType", "Sport type cannot be empty.");
            valid = false;
        }

        if (!valid) {
            request.setAttribute("update", true);
            CategoryDTO old = new CategoryDAO().getCategoryByID(categoryID);
            request.setAttribute("c", old);
            request.getRequestDispatcher("admin/categoryForm.jsp").forward(request, response);
            return;
        }

        // Cập nhật
        CategoryDTO category = new CategoryDTO(categoryID, categoryName, sportType, true);
        CategoryDAO dao = new CategoryDAO();

        boolean success = dao.update(category);

        if (success) {
            response.sendRedirect("MainController?txtAction=viewCategoryList");
        } else {
            request.setAttribute("error", "Failed to update category!");
            request.setAttribute("update", true);
            request.setAttribute("c", category);
            request.getRequestDispatcher("admin/categoryForm.jsp").forward(request, response);
        }
    }

    private void processViewCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> listCategories = categoryDAO.getAllCategory();

        request.setAttribute("listCategories", listCategories);
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");

        if (txtAction.equals("viewCategoryList")) {
            processViewCategoryList(request, response);
        } else if (txtAction.equals("callCategoryForm")) {
            processCallCategoryForm(request, response);
        } else if (txtAction.equals("filterCategory")) {
            processFilterCategory(request, response);
        } else if ("addCategory".equals(txtAction)) {
            processAddCategory(request, response);
        } else if ("updateCategory".equals(txtAction)) {
            processUpdateCategory(request, response);
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
