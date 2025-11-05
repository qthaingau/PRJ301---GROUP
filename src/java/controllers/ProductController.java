/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
public class ProductController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processCallSaveProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String updateParam = request.getParameter("update");

// Chuyển đổi chuỗi "true" hoặc "false" thành boolean
        boolean isUpdate = Boolean.parseBoolean(updateParam);

        request.setAttribute("update", isUpdate);
        request.getRequestDispatcher("/admin/productForm.jsp")
                .forward(request, response);
    }

    private void processAddProductWithVariant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Regex for IDs: P******, C******, B******, V******
        String regexP = "^P\\d{6}$";
        String regexC = "^C\\d{6}$";
        String regexB = "^B\\d{6}$";
        String regexV = "^V\\d{6}$";

        // --- Read form parameters ---
        String productID = request.getParameter("txtProductID");
        String productName = request.getParameter("txtProductName");
        String description = request.getParameter("txtDescription");
        String categoryID = request.getParameter("txtCategoryID");
        String brandID = request.getParameter("txtBrandID");

        String variantID = request.getParameter("txtVariantID");
        String size = request.getParameter("txtSize");
        String color = request.getParameter("txtColor");
        String stockStr = request.getParameter("txtStock");
        String priceStr = request.getParameter("txtPrice");

        // --- Prepare DAOs ---
        ProductDAO productDAO = new ProductDAO();
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        // --- Error messages ---
        String error_productID = "";
        String error_productName = "";
        String error_description = "";
        String error_categoryID = "";
        String error_brandID = "";
        String error_variantID = "";
        String error_size = "";
        String error_color = "";
        String error_stock = "";
        String error_price = "";

        boolean hasError = false;

        // --- Validate ProductID ---
        if (productID == null || productID.trim().isEmpty()) {
            error_productID = "Product ID cannot be empty!";
            hasError = true;
        } else if (!productID.trim().matches(regexP)) {
            error_productID = "Product ID must follow format P****** (e.g., P000001).";
            hasError = true;
        } else {
            // Check duplicate productID ONLY when adding new product
            if (productDAO.getProductByID(productID.trim()) != null) {
                error_productID = "Product ID is duplicated!";
                hasError = true;
            }
        }

        // --- Validate Product Name ---
        if (productName == null || productName.trim().isEmpty()) {
            error_productName = "Product name cannot be empty!";
            hasError = true;
        }

        // --- Validate Description ---
        if (description == null || description.trim().isEmpty()) {
            error_description = "Description cannot be empty!";
            hasError = true;
        }

        // --- Validate CategoryID ---
        if (categoryID == null || categoryID.trim().isEmpty()) {
            error_categoryID = "Category ID cannot be empty!";
            hasError = true;
        } else if (!categoryID.trim().matches(regexC)) {
            error_categoryID = "Category ID must follow format C****** (e.g., C000001).";
            hasError = true;
        }

        // --- Validate BrandID ---
        if (brandID == null || brandID.trim().isEmpty()) {
            error_brandID = "Brand ID cannot be empty!";
            hasError = true;
        } else if (!brandID.trim().matches(regexB)) {
            error_brandID = "Brand ID must follow format B****** (e.g., B000001).";
            hasError = true;
        }

        // --- Validate VariantID ---
        if (variantID == null || variantID.trim().isEmpty()) {
            error_variantID = "Variant ID cannot be empty!";
            hasError = true;
        } else if (!variantID.trim().matches(regexV)) {
            error_variantID = "Variant ID must follow format V****** (e.g., V000001).";
            hasError = true;
        } else if (variantDAO.getVariantByID(variantID.trim()) != null) {
            error_variantID = "Variant ID already exists!";
            hasError = true;
        }

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

        // --- Validate Stock & Price ---
        int stock = 0;
        double price = 0;

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

        // --- Create DTOs (even if there are errors, to keep user input) ---
        ProductDTO product = new ProductDTO(productID, productName, description,
                categoryID, brandID, null, true);
        ProductVariantDTO variant = new ProductVariantDTO(variantID, productID, size,
                color, stock, price, 0);

        String error = "";

        // --- If no validation error, try to insert into DB ---
        if (!hasError) {
            try {
                boolean ok1 = productDAO.insert(product);
                boolean ok2 = variantDAO.insert(variant);

                if (!ok1 || !ok2) {
                    error = "Failed to add product or variant!";
                    hasError = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = "System error: " + e.getMessage();
                hasError = true;
            }
        }

        // --- If there is any error -> forward back to form with messages ---
        if (hasError) {
            // Keep entered data so user doesn't have to retype everything
            request.setAttribute("p", product);
            request.setAttribute("v", variant);

            request.setAttribute("error_productID", error_productID);
            request.setAttribute("error_productName", error_productName);
            request.setAttribute("error_description", error_description);
            request.setAttribute("error_categoryID", error_categoryID);
            request.setAttribute("error_brandID", error_brandID);
            request.setAttribute("error_variantID", error_variantID);
            request.setAttribute("error_size", error_size);
            request.setAttribute("error_color", error_color);
            request.setAttribute("error_stock", error_stock);
            request.setAttribute("error_price", error_price);
            request.setAttribute("error", error);

            request.getRequestDispatcher("productForm.jsp").forward(request, response);
            return;
        }

        // --- If everything OK -> go back to product list (similar to processSearchUser) ---
        response.sendRedirect("MainController?txtAction=viewProducts");
    }

    private void processFilterProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        ProductDAO productDAO = new ProductDAO();
        List<ProductDTO> list;

        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                list = productDAO.getAllProduct();
            } else {
                list = productDAO.filterProduct(keyword.trim());
            }
            request.setAttribute("listProducts", list);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error searching product!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    private void processViewProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("productID");
        String productName = request.getParameter("productName");

        ProductVariantDAO productDAO = new ProductVariantDAO();
        ProductVariantDTO productDTO = null;

        try {
            productDTO = productDAO.getVariantByProductID(keyword);
            request.setAttribute("productDetail", productDTO);
            request.setAttribute("productID", keyword);
            HttpSession session = request.getSession();
            session.setAttribute("productName", productName);
            request.getRequestDispatcher("/customer/productDetail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error viewing product detail!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void processViewProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            try {
                ProductDAO productDAO = new ProductDAO();
                List<ProductDTO> listProducts = productDAO.getAllProduct();
                HttpSession session = request.getSession();
                session.setAttribute("listProducts", listProducts);
                request.getRequestDispatcher("home.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "Error loading product list!");
                request.getRequestDispatcher("home.jsp").forward(request, response);
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String txtAction = request.getParameter("txtAction");

            if (txtAction == null) {
                txtAction = "viewProducts";
            }

            if (txtAction.equals("viewProducts")) {
                processViewProducts(request, response);
            } else if (txtAction.equals("filterProduct")) {
                processFilterProduct(request, response);
            } else if (txtAction.equals("viewProductDetail")) {
                processViewProductDetail(request, response);
            } else if (txtAction.equals("addProduct")) {
                processAddProductWithVariant(request, response);
            } else if (txtAction.equals("callSaveProduct")) {
                processCallSaveProduct(request, response);
            }
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
