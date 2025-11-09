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
import models.BrandDAO;
import models.CategoryDAO;
import models.ProductDAO;
import models.ProductDTO;
import models.ProductVariantDAO;
import models.ProductVariantDTO;
import models.UserDTO;

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

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    // Giữ lại để JSP biết đang ở mode Add hay Update
    request.setAttribute("update", update);

    // Regex cho ID: PV***, P***
    // Variant: PV001, PV123...
    String regexV = "^PV\\d{3}$";   // đã toUpperCase nên chỉ cần PV
    String regexP = "^P\\d{3}$";    // Product: P001, P123...

    // --- Read form parameters (VARIANT + ProductID) ---
    String variantID = request.getParameter("txtVariantID");
    if (variantID != null) {
        variantID = variantID.toUpperCase();
    }

    String productID = request.getParameter("txtProductID");
    if (productID != null) {
        productID = productID.toUpperCase();
    }

    String size = request.getParameter("txtSize");
    String color = request.getParameter("txtColor");
    String stockStr = request.getParameter("txtStock");
    String priceStr = request.getParameter("txtPrice");
    String variantImage = request.getParameter("txtVariantImage");

    // --- Prepare DAOs ---
    ProductVariantDAO variantDAO = new ProductVariantDAO();
    ProductDAO productDAO = new ProductDAO();

    // --- Error messages ---
    String error_variantID = "";
    String error_productID = "";
    String error_size = "";
    String error_color = "";
    String error_stock = "";
    String error_price = "";
    String error = "";

    boolean hasError = false;

    int stock = 0;
    double price = 0;

    // --- Validate VariantID ---
    ProductVariantDTO existingVariant = null;
    if (variantID == null || variantID.trim().isEmpty()) {
        error_variantID = "Variant ID cannot be empty!";
        hasError = true;
    } else if (!variantID.trim().matches(regexV)) {
        error_variantID = "Variant ID must follow format PV*** (e.g., PV001).";
        hasError = true;
    } else {
        try {
            existingVariant = variantDAO.getVariantByID(variantID.trim());
        } catch (Exception e) {
            e.printStackTrace();
            error = "System error when checking Variant ID: " + e.getMessage();
            hasError = true;
        }

        if (!hasError) {
            if (!update) { // ADD: không được trùng
                if (existingVariant != null) {
                    error_variantID = "Variant ID is duplicated!";
                    hasError = true;
                }
            } else {       // UPDATE: bắt buộc phải tồn tại
                if (existingVariant == null) {
                    error_variantID = "Variant ID does not exist!";
                    hasError = true;
                }
            }
        }
    }

    // --- Validate ProductID (variant phải thuộc 1 product đã tồn tại) ---
    if (productID == null || productID.trim().isEmpty()) {
        error_productID = "Product ID cannot be empty!";
        hasError = true;
    } else if (!productID.trim().matches(regexP)) {
        error_productID = "Product ID must follow format P*** (e.g., P001).";
        hasError = true;
    } else {
        try {
            if (productDAO.getProductByID(productID.trim()) == null) {
                error_productID = "Product ID does not exist!";
                hasError = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "System error when checking Product ID: " + e.getMessage();
            hasError = true;
        }
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

    // --- Create Variant DTO ---
    ProductVariantDTO variant = new ProductVariantDTO();
    variant.setVariantID(variantID);
    variant.setProductID(productID);
    variant.setSize(size);
    variant.setColor(color);
    variant.setStock(stock);
    variant.setPrice(price);
    variant.setVariantImage(variantImage);
    // salesCount nếu có thì set thêm ở đây, hoặc default trong DB

    // --- Nếu không có lỗi validate thì insert/update DB ---
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

    // --- Nếu có lỗi: quay lại form + đẩy error ---
    if (hasError) {
        // set lại DTO để fill form
        request.setAttribute("v", variant);

        // productID gói trong 1 ProductDTO tối giản
        ProductDTO p = new ProductDTO();
        p.setProductID(productID);
        request.setAttribute("p", p);

        request.setAttribute("error_variantID", error_variantID);
        request.setAttribute("error_productID", error_productID);
        request.setAttribute("error_size", error_size);
        request.setAttribute("error_color", error_color);
        request.setAttribute("error_stock", error_stock);
        request.setAttribute("error_price", error_price);
        request.setAttribute("error", error);

        request.getRequestDispatcher("admin/variantForm.jsp").forward(request, response);
        return;
    }

    // --- OK thì quay về list variant ---
    response.sendRedirect("MainController?txtAction=viewVariantList");
}


    private void processDeleteVariant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Lấy 2 tham số từ URL (variantID & productID)
        String variantID = request.getParameter("variantID");
        String productID = request.getParameter("productID");

        ProductVariantDAO variantDAO = new ProductVariantDAO();

        try {
            // Kiểm tra null/empty
            if (variantID == null || variantID.trim().isEmpty()) {
                request.setAttribute("msg", "Variant ID is missing!");
                request.getRequestDispatcher("MainController?txtAction=viewVariantList").forward(request, response);
                return;
            }

            if (productID == null || productID.trim().isEmpty()) {
                request.setAttribute("msg", "Product ID is missing!");
                request.getRequestDispatcher("MainController?txtAction=viewVariantList").forward(request, response);
                return;
            }

            boolean result = variantDAO.delete(variantID.trim(), productID.trim());

            if (result) {
                // Xoá thành công → reload danh sách variant
                response.sendRedirect("MainController?txtAction=viewVariantList");
            } else {
                // Xoá thất bại → báo lỗi
                request.setAttribute("msg", "Failed to delete variant!");
                request.getRequestDispatcher("admin/listOfVariants.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error while deleting variant: " + e.getMessage());
            request.getRequestDispatcher("admin/listOfVariants.jsp").forward(request, response);
        }
    }

    private void processCallSaveVariant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy cờ update từ request (true/false)
        String updateParam = request.getParameter("update");
        boolean isUpdate = Boolean.parseBoolean(updateParam);

        // Lấy id từ request
        String variantID = request.getParameter("variantID");
        String productID = request.getParameter("productID");

        // Chuẩn bị DAO
        ProductDAO productDAO = new ProductDAO();
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        ProductDTO productDTO = null;
        ProductVariantDTO variantDTO = null;

        try {
            // ===== Trường hợp UPDATE =====
            if (isUpdate && variantID != null && !variantID.isEmpty()) {
                variantDTO = variantDAO.getVariantByID(variantID);

                if (variantDTO != null) {
                    // Nếu variant có productID hợp lệ thì lấy thông tin product tương ứng
                    productDTO = productDAO.getProductByID(variantDTO.getProductID());
                }

                // ===== Trường hợp ADD variant =====
            } else if (!isUpdate) {
                // Nếu có productID -> kiểm tra tồn tại trong DB
                if (productID != null && !productID.isEmpty()) {
                    ProductDTO existingProduct = productDAO.getProductByID(productID.trim());

                    if (existingProduct != null) {
                        productDTO = existingProduct; // Gán thông tin product để hiển thị tự động
                    } else {
                        // Nếu không tồn tại, tạo đối tượng trống chỉ để fill ID
                        productDTO = new ProductDTO();
                        productDTO.setProductID(productID.trim().toUpperCase());
                        request.setAttribute("msgWarning", "⚠ Product ID does not exist in database — please recheck.");
                    }
                }
            }

            // ===== Truyền dữ liệu sang JSP =====
            request.setAttribute("update", isUpdate);
            if (variantDTO != null) {
                request.setAttribute("v", variantDTO);
            }
            if (productDTO != null) {
                request.setAttribute("p", productDTO);
            }

            // list variant (để kiểm tra trùng ID ở JS)
            HttpSession session = request.getSession();
            session.setAttribute("variantList", variantDAO.getAllVariants());

            // Forward sang form variant
            request.getRequestDispatcher("/admin/variantForm.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error loading variant form: " + e.getMessage());
            request.getRequestDispatcher("admin/listOfVariants.jsp").forward(request, response);
        }
    }

    private void processFilterVariant(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String keyword = request.getParameter("keyword");
    ProductVariantDAO variantDAO = new ProductVariantDAO();
    List<ProductVariantDTO> list;

    try {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Không nhập gì -> lấy toàn bộ variant
            list = variantDAO.getAllVariants();   // đảm bảo bạn đã có hàm này
        } else {
            // Có keyword -> filter
            list = variantDAO.filterVariant(keyword.trim());
        }

        request.setAttribute("listVariants", list);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("admin/listOfVariants.jsp")
               .forward(request, response);

    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("msg", "Error searching variant!");
        request.getRequestDispatcher("admin/listOfVariants.jsp")
               .forward(request, response);
    }
}


    private void processViewVariants(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession(true);
        }

        // Lấy user đang login (nếu có) – giống processViewProducts
        UserDTO loginUser = (UserDTO) session.getAttribute("user");

        boolean isAdminOrStaff = false;
        if (loginUser != null) {
            String role = loginUser.getRole(); // đúng với getter của bạn
            if ("admin".equalsIgnoreCase(role) || "staff".equalsIgnoreCase(role)) {
                isAdminOrStaff = true;
            }
        }
        session.removeAttribute("listVariants");

        try {
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            List<ProductVariantDTO> listVariants;

            // Nếu sau này muốn giới hạn cho customer/guest thì thêm DAO getActiveVariants() ở đây
            if (isAdminOrStaff) {
                // Admin / Staff: xem tất cả variant
                listVariants = variantDAO.getAllVariants();
            } else {
                // Hiện tại mình vẫn cho xem tất cả.
                // Nếu bạn muốn chỉ xem variant active thì tạo:
                // listVariants = variantDAO.getActiveVariants();
                listVariants = variantDAO.getAllVariants();
            }

            session.setAttribute("listVariants", listVariants);

            request.getRequestDispatcher("admin/listOfVariants.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error loading variant list!");
            request.getRequestDispatcher("admin/listOfVariants.jsp").forward(request, response);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String txtAction = request.getParameter("txtAction");

        if (txtAction == null) {
            txtAction = "";
        }
        System.out.println("nè" + txtAction);

        try {
            if (txtAction.equals("viewVariantList")) {
                processViewVariants(request, response);

            } else if (txtAction.equals("callSaveVariant")) {
                processCallSaveVariant(request, response);

            } else if (txtAction.equals("addVariant")) {
                processSaveVariant(request, response, false);

            } else if (txtAction.equals("updateVariant")) {
                processSaveVariant(request, response, true);

            } else if (txtAction.equals("deleteVariant")) {
                processDeleteVariant(request, response);

            } else if (txtAction.equals("filterVariant")) {
                processFilterVariant(request, response);

            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error processing variant request: " + e.getMessage());
            request.getRequestDispatcher("admin/listOfVariants.jsp").forward(request, response);
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
