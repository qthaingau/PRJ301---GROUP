/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.ProductDAO;
import models.ProductDTO;
import models.ProductVariantDAO;
import models.ProductVariantDTO;

/**
 *
 * @author TEST
 */
@WebServlet(name = "VariantController", urlPatterns = {"/VariantController"})
public class VariantController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processSaveVariant(HttpServletRequest request, HttpServletResponse response, boolean update)
            throws ServletException, IOException {

        // Cho JSP biết đang ở mode Add hay Update
        request.setAttribute("update", update);

        // Regex cho ID
        String regexP = "^P\\d{3}$";    // Product ID: P***
        String regexV = "^PV\\d{3}$";   // Variant ID: PV***

        // --- Read form parameters ---
        String variantID = request.getParameter("txtVariantID");
        String productID = request.getParameter("txtProductID");
        String size = request.getParameter("txtSize");
        String color = request.getParameter("txtColor");
        String stockStr = request.getParameter("txtStock");
        String priceStr = request.getParameter("txtPrice");
        String variantImage = request.getParameter("txtVariantImage");

        // --- DAOs ---
        ProductDAO productDAO = new ProductDAO();
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        // --- Error messages ---
        String error_variantID = "";
        String error_productID = "";
        String error_size = "";
        String error_color = "";
        String error_stock = "";
        String error_price = "";
        String error = "";

        boolean hasError = false;

        // Lấy variant cũ khi update để giữ salesCount
        ProductVariantDTO oldVariant = null;
        if (update && variantID != null && !variantID.trim().isEmpty()) {
            oldVariant = variantDAO.getVariantByID(variantID.trim());
        }

        // --- Validate ProductID (FK) ---
        if (productID == null || productID.trim().isEmpty()) {
            error_productID = "Product ID cannot be empty!";
            hasError = true;
        } else if (!productID.trim().matches(regexP)) {
            error_productID = "Product ID must follow format P*** (e.g., P001).";
            hasError = true;
        } else if (productDAO.getProductByID(productID.trim()) == null) {
            error_productID = "Product ID does not exist!";
            hasError = true;
        }

        // --- Validate VariantID ---
        if (variantID == null || variantID.trim().isEmpty()) {
            error_variantID = "Variant ID cannot be empty!";
            hasError = true;
        } else if (!variantID.trim().matches(regexV)) {
            error_variantID = "Variant ID must follow format PV*** (e.g., PV001).";
            hasError = true;
        } else {
            if (!update) { // ADD: không được trùng
                if (variantDAO.getVariantByID(variantID.trim()) != null) {
                    error_variantID = "Variant ID already exists!";
                    hasError = true;
                }
            } else { // UPDATE: bắt buộc phải tồn tại
                if (oldVariant == null) {
                    error_variantID = "Variant ID does not exist in database!";
                    hasError = true;
                }
            }
        }

        int stock = 0;
        double price = 0.0;

        // --- Validate Size ---
        if (size == null || size.trim().isEmpty()) {
            error_size = "Size cannot be empty!";
            hasError = true;
        }

        // --- Validate Color ---
        if (color == null || color.trim().isEmpty()) {
            error_color = "Color cannot be empty!";
            hasError = true;
        }

        // --- Validate Stock ---
        if (stockStr == null || stockStr.trim().isEmpty()) {
            error_stock = "Stock cannot be empty!";
            hasError = true;
        } else {
            try {
                stock = Integer.parseInt(stockStr.trim());
                if (stock < 0) {
                    error_stock = "Stock cannot be negative!";
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                error_stock = "Stock must be a valid integer!";
                hasError = true;
            }
        }

        // --- Validate Price ---
        if (priceStr == null || priceStr.trim().isEmpty()) {
            error_price = "Price cannot be empty!";
            hasError = true;
        } else {
            try {
                price = Double.parseDouble(priceStr.trim());
                if (price < 0) {
                    error_price = "Price cannot be negative!";
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                error_price = "Price must be a valid number!";
                hasError = true;
            }
        }

        // --- Create DTO ---
        int salesCount = 0;
        if (update && oldVariant != null) {
            salesCount = oldVariant.getSalesCount();
        }

        ProductVariantDTO variant = new ProductVariantDTO(
                variantID,
                productID,
                size,
                color,
                stock,
                price,
                salesCount,
                variantImage
        );

        // --- Nếu không có lỗi -> insert / update DB ---
        if (!hasError) {
            boolean ok = false;

            try {
                if (update) {
                    ok = variantDAO.update(variant);
                } else {
                    ok = variantDAO.insert(variant);
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = "System error: " + e.getMessage();
                hasError = true;
            }

            if (!ok) {
                error = update
                        ? "Failed to update variant!"
                        : "Failed to add variant!";
                hasError = true;
            }
        }

        // --- Nếu có lỗi: quay lại form variant ---
        if (hasError) {
            request.setAttribute("v", variant);

            request.setAttribute("error_variantID", error_variantID);
            request.setAttribute("error_productID", error_productID);
            request.setAttribute("error_size", error_size);
            request.setAttribute("error_color", error_color);
            request.setAttribute("error_stock", error_stock);
            request.setAttribute("error_price", error_price);
            request.setAttribute("error", error);

            // Đổi đường dẫn JSP cho đúng file form variant của bạn
            request.getRequestDispatcher("/admin/variantForm.jsp").forward(request, response);
            return;
        }

        // --- Thành công: quay lại trang detail product (hoặc list variant tuỳ bạn) ---
        response.sendRedirect("MainController?txtAction=viewProductDetail&productID=" + productID);
    }

    private void processCallSaveVariant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy cờ update từ request (true/false)
        String updateParam = request.getParameter("update");
        boolean isUpdate = Boolean.parseBoolean(updateParam);

        // Lấy id từ request
        String productID = request.getParameter("productID");
        String variantID = request.getParameter("variantID");

        ProductDAO productDAO = new ProductDAO();
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        // Nếu UPDATE và có đủ productID + variantID -> load dữ liệu variant lên form
        if (isUpdate && productID != null && !productID.isEmpty()
                && variantID != null && !variantID.isEmpty()) {

            ProductDTO productDTO = productDAO.getProductByID(productID);
            ProductVariantDTO variantDTO = variantDAO.getVariantByID(variantID);

            request.setAttribute("p", productDTO); // nếu form cần hiển thị info product
            request.setAttribute("v", variantDTO);
        } else if (!isUpdate && productID != null && !productID.isEmpty()) {
            // Trường hợp ADD variant mới cho 1 product cụ thể -> vẫn nên load product để hiển thị
            ProductDTO productDTO = productDAO.getProductByID(productID);
            request.setAttribute("p", productDTO);
        }

        // Luôn set cờ update cho JSP
        request.setAttribute("update", isUpdate);

        HttpSession session = request.getSession();
        // Nếu form variant cần list variant để show đâu đó (không bắt buộc)
        session.setAttribute("variantList", variantDAO.getAllVariants());

        // Forward sang form variant (KHÁC productForm.jsp)
        request.getRequestDispatcher("/admin/variantForm.jsp")
                .forward(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");
        if (txtAction.equals("addVariant")) {
            processSaveVariant(request, response, true);
        } else if (txtAction.equals("updateVariant")) {
            processSaveVariant(request, response, true);
        } else if (txtAction.equals("callSaveVariant")) {
            processCallSaveVariant(request, response);
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
