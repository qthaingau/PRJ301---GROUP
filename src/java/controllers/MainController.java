package controllers;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String txtAction = request.getParameter("txtAction");
        String url = "home.jsp"; // default

        String[] userActions = {"login", "logout", "searchUser", "addUser",
            "callUpdateUser", "updateUser", "deleteUser", "registerUser", "showRegister", "changePassword", "uploadAvatar"};

        String[] productActions = {"viewProducts", "addProduct", "deleteProduct",
            "viewProductDetail", "filterProduct",
            "callSaveProduct", "addProductWithVariant", "updateProductWithVariant", "deleteProductWithVariant", "toggleProductStatus"};

        String[] brandActions = {"viewBrandList", "updateBrand", "addBrand", "filterBrand", "callBrandForm", "deleteBrand"};
        String[] categoryActions = {"viewCategoryList", "updateCategory", "addCategory", "callCategoryForm", "filterCategory", "deleteCategory"};

        String[] cartActions = {"viewCart", "addToCart", "updateCart", "removeFromCart"};

        // Trong MainController.java, thêm vào đầu class:
        String[] addressActions = {
            "viewAddressList", "showAddAddressForm", "addAddress",
            "deleteAddress", "setDefaultAddress"
        };
        // Trong try block, thêm:

        try {
            if (txtAction != null) {
                if (Arrays.asList(userActions).contains(txtAction)) {
                    url = "UserController";
                } else if (Arrays.asList(productActions).contains(txtAction)) {
                    url = "ProductController";
                } else if (Arrays.asList(categoryActions).contains(txtAction)) {
                    url = "CategoryController";
                } else if (Arrays.asList(brandActions).contains(txtAction)) {
                    url = "BrandController";
                } else if (Arrays.asList(cartActions).contains(txtAction)) {
                    url = "CartController"; // Route qua CartController
                } else if (Arrays.asList(addressActions).contains(txtAction)) {
                    url = "AddressController";
                } else if ("home".equals(txtAction)) {
                    url = "BrandController";
                }
            } else {
                // Nếu không có txtAction → về home
                request.getRequestDispatcher("home.jsp").forward(request, response);
                return;
            }

            request.getRequestDispatcher(url).forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error in MainController!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
        return "MainController - route tất cả action";
    }
}
