/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private void processCallSaveOnlyProduct(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String updateParam = request.getParameter("update");
    boolean isUpdate = Boolean.parseBoolean(updateParam);

    String productID = request.getParameter("productID");
    if (productID != null) productID = productID.trim();

    ProductDAO productDAO = new ProductDAO();
//    ProductVariantDAO variantDAO = new ProductVariantDAO();
    BrandDAO brandDAO = new BrandDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

//    System.out.println(">>> processCallSaveOnlyProduct: update=" + isUpdate + ", productID=[" + productID + "]");

    if (isUpdate && productID != null && !productID.isEmpty()) {
        ProductDTO productDTO = productDAO.getProductByID(productID);
        request.setAttribute("p", productDTO);
    }

    request.setAttribute("update", isUpdate);

    // Dùng request scope thay vì session để tránh lỗi cache hoặc trống
//    request.setAttribute("variantList", variantDAO.getAllVariantsByProductID(productID));
    request.setAttribute("brandList", brandDAO.getActiveBrands());
    request.setAttribute("categoryList", categoryDAO.getActiveCategories());

    request.getRequestDispatcher("/admin/productForm.jsp").forward(request, response);
}

    
    private void processCallSaveProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy cờ update từ request (true/false)
        String updateParam = request.getParameter("update");
        boolean isUpdate = Boolean.parseBoolean(updateParam);

        // Lấy id từ request (nếu có)
        String productID = request.getParameter("productID");
        String variantID = request.getParameter("variantID");

        ProductDAO productDAO = new ProductDAO();
        ProductVariantDAO variantDAO = new ProductVariantDAO();
        BrandDAO brandDAO = new BrandDAO(); // NEW
        CategoryDAO categoryDAO = new CategoryDAO();

        // Nếu là update và có productID, variantID thì load dữ liệu lên form
        if (isUpdate && productID != null && !productID.isEmpty()
                && variantID != null && !variantID.isEmpty()) {

            ProductDTO productDTO = productDAO.getProductByID(productID);
            ProductVariantDTO variantDTO = variantDAO.getVariantByID(variantID);

            request.setAttribute("p", productDTO);
            request.setAttribute("v", variantDTO);
        }

        // Luôn set cờ update cho JSP
        request.setAttribute("update", isUpdate);

        HttpSession session = request.getSession();
        // NEW: luôn load danh sách brand đang active cho dropdown
        session.setAttribute("variantList", variantDAO.getAllVariants());
        session.setAttribute("brandList", brandDAO.getActiveBrands());
        session.setAttribute("categoryList", categoryDAO.getActiveCategories());

        // Forward sang form
        request.getRequestDispatcher("/admin/productForm.jsp")
                .forward(request, response);
    }

    private void processSaveProduct(HttpServletRequest request, HttpServletResponse response, boolean update)
        throws ServletException, IOException {

    // Giữ lại để JSP biết đang ở mode Add hay Update
    request.setAttribute("update", update);

    // Regex cho ID: P***, C***, B***
    String regexP = "^[pP]\\d{3}$";
    String regexC = "^[cC]\\d{3}$";
    String regexB = "^[bB]\\d{3}$";

    // --- Read form parameters (CHỈ PRODUCT) ---
    String productID = request.getParameter("txtProductID").toUpperCase();
    String productName = request.getParameter("txtProductName");
    String description = request.getParameter("txtDescription");
    String categoryID = request.getParameter("txtCategoryID").toUpperCase();
    String brandID = request.getParameter("txtBrandID").toUpperCase();
    String productImage = request.getParameter("txtProductImage");

    // --- Prepare DAOs ---
    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    BrandDAO brandDAO = new BrandDAO();

    // --- Error messages ---
    String error_productID = "";
    String error_productName = "";
    String error_description = "";
    String error_categoryID = "";
    String error_brandID = "";
    String error = "";

    boolean hasError = false;

    // --- Validate ProductID ---
    if (productID == null || productID.trim().isEmpty()) {
        error_productID = "Product ID cannot be empty!";
        hasError = true;
    } else if (!productID.trim().matches(regexP)) {
        error_productID = "Product ID must follow format P*** (e.g., P001).";
        hasError = true;
    } else {
        if (!update) { // ADD: không được trùng
            if (productDAO.getProductByID(productID.trim()) != null) {
                error_productID = "Product ID is duplicated!";
                hasError = true;
            }
        } else { // UPDATE: bắt buộc phải tồn tại
            if (productDAO.getProductByID(productID.trim()) == null) {
                error_productID = "Product ID does not exist!";
                hasError = true;
            }
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
        error_categoryID = "Category ID must follow format C*** (e.g., C001).";
        hasError = true;
    } else if (categoryDAO.getCategoryByID(categoryID.trim()) == null) {
        error_categoryID = "Category ID does not exist!";
        hasError = true;
    }

    // --- Validate BrandID ---
    if (brandID == null || brandID.trim().isEmpty()) {
        error_brandID = "Brand ID cannot be empty!";
        hasError = true;
    } else if (!brandID.trim().matches(regexB)) {
        error_brandID = "Brand ID must follow format B*** (e.g., B001).";
        hasError = true;
    } else if (brandDAO.getBrandByID(brandID.trim()) == null) {
        error_brandID = "Brand ID does not exist!";
        hasError = true;
    }

    // --- Create Product DTO ---
    ProductDTO product = new ProductDTO(
            productID,
            productName,
            description,
            categoryID,
            brandID,
            null,       // tuỳ constructor của bạn: có thể là createdDate / updatedDate
            true,       // isActive
            productImage
    );

    // --- Nếu không có lỗi validate thì insert/update DB ---
    if (!hasError) {
        boolean ok1 = false;

        try {
            if (update) {
                ok1 = productDAO.update(product);
            } else {
                ok1 = productDAO.insert(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = "System error: " + e.getMessage();
            hasError = true;
        }

        if (!ok1) {
            error = update
                    ? "Failed to update product!"
                    : "Failed to add product!";
            hasError = true;
        }
    }

    // --- Nếu có lỗi: quay lại form + đẩy error ---
    if (hasError) {
        request.setAttribute("p", product);

        request.setAttribute("error_productID", error_productID);
        request.setAttribute("error_productName", error_productName);
        request.setAttribute("error_description", error_description);
        request.setAttribute("error_categoryID", error_categoryID);
        request.setAttribute("error_brandID", error_brandID);
        request.setAttribute("error", error);

        request.getRequestDispatcher("/admin/productForm.jsp").forward(request, response);
        return;
    }

    // --- OK thì quay về list product ---
    response.sendRedirect("MainController?txtAction=viewProductList");
}


    private void processDeleteWithVariant(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pID = request.getParameter("productID");
        String vID = request.getParameter("variantID");

        ProductVariantDAO variantDAO = new ProductVariantDAO();
        boolean deleted = variantDAO.delete(vID, pID);

        if (deleted) {
            request.getRequestDispatcher("admin/listOfProducts.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "Xoá thất bại!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void processDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pID = request.getParameter("productID");

        ProductVariantDAO variantDAO = new ProductVariantDAO();
        List<ProductVariantDTO> listVariant = variantDAO.getAllVariantsByProductID(pID);

        // Giả định xoá thành công, nếu có 1 cái fail thì set = false
        boolean deletedAll = true;

        if (listVariant != null && !listVariant.isEmpty()) {
            for (ProductVariantDTO v : listVariant) {
                boolean ok = variantDAO.delete(v.getVariantID(), pID); // dùng đúng variantID
                if (!ok) {
                    deletedAll = false;
                    break; // 1 cái lỗi là đủ
                }
            }
        }

        if (deletedAll) {
            // redirect về list để tránh lỗi F5 bị submit lại
            response.sendRedirect("MainController?txtAction=viewProductList");
        } else {
            request.setAttribute("msg", "Xoá thất bại!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void processFilterProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String txtAction = request.getParameter("txtAction");
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
            if (txtAction.equals("filterProductList")) {
                request.getRequestDispatcher("admin/listOfProducts.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("home.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error searching product!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    private void processViewProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productID = request.getParameter("productID");
        if (productID == null || productID.trim().isEmpty()) {
            response.sendRedirect("MainController?txtAction=viewProducts");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        ProductDTO product = productDAO.getProductByID(productID);
        if (product == null) {
            request.setAttribute("msg", "Sản phẩm không tồn tại!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Lấy tất cả variant
        List<ProductVariantDTO> allVariants = variantDAO.getAllVariantsByProductID(productID);

        // GỘP THEO COLOR + PRICE + IMAGE (dùng Map)
        Map<String, List<ProductVariantDTO>> grouped = new LinkedHashMap<>();
        for (ProductVariantDTO v : allVariants) {
            String key = v.getColor() + "|" + v.getPrice() + "|" + (v.getVariantImage() != null ? v.getVariantImage() : "");
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(v);
        }

        // Gửi Map trực tiếp vào JSP
        request.setAttribute("product", product);
        request.setAttribute("groupedVariants", grouped);

        request.getRequestDispatcher("/customer/productDetail.jsp").forward(request, response);
    }

    private void processViewProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Lấy session hiện tại (nếu có)
        HttpSession session = request.getSession(false);

        // Nếu session đã tồn tại, xóa danh sách sản phẩm cũ
        if (session != null) {
            session.removeAttribute("listProducts");  // Xóa danh sách sản phẩm cũ trong session
        } else {
            session = request.getSession(true); // Nếu không có session, tạo session mới
        }

        try {
            // Lấy danh sách sản phẩm mới nhất từ ProductDAO
            ProductDAO productDAO = new ProductDAO();
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            List<ProductDTO> listProducts = productDAO.getAllProduct();
            String txtAction = request.getParameter("txtAction");

            // Đưa listProducts vào session
            session.setAttribute("listProducts", listProducts);
            session.setAttribute("variantList", variantDAO.getAllVariants());

            // Chuyển hướng tới trang home.jsp
            if (txtAction.equals("viewProducts")) {
                request.getRequestDispatcher("home.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("admin/listOfProducts.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error loading product list!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    private void processToggleProductStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productID = request.getParameter("productID");
        System.out.println("processToggleProductStatus: productID = " + productID);

        if (productID != null && !productID.isEmpty()) {
            ProductDAO productDAO = new ProductDAO();
            boolean ok = productDAO.toggleStatus(productID);
            System.out.println("toggleStatus() result = " + ok);
        }

        // quay lại list qua MainController (để dùng lại group router)
        response.sendRedirect("MainController?txtAction=viewProductList");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String txtAction = request.getParameter("txtAction");
            System.out.println("Nè " + txtAction);

            if (txtAction == null) {
                txtAction = "viewProducts";
            }

            if (txtAction.equals("viewProducts") || txtAction.equals("viewProductList")) {
                processViewProducts(request, response);
            } else if (txtAction.equals("filterProduct") || txtAction.equals("filterProductList")) {
                processFilterProduct(request, response);
            } else if (txtAction.equals("viewProductDetail")) {
                processViewProductDetail(request, response);
            } else if (txtAction.equals("addProduct")) {
                processSaveProduct(request, response, false);
            } else if (txtAction.equals("callSaveProduct")) {
                processCallSaveProduct(request, response);
            } else if (txtAction.equals("updateProduct")) {
                processSaveProduct(request, response, true);
            } else if (txtAction.equals("deleteProduct")) {
                processDeleteProduct(request, response);
            } else if (txtAction.equals("toggleProductStatus")) {
                processToggleProductStatus(request, response);
            } else if (txtAction.equals("callSaveOnlyProduct")) {
                processCallSaveOnlyProduct(request, response);
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
