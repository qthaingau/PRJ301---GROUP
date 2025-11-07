/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tungi
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        // request.getParameter
        String txtAction = request.getParameter("txtAction");

        String url = "home.jsp";       // chú ý dấu / và đúng path file login

        String[] userActions = {"login", "logout", "searchUser", "addUser",
            "callUpdateUser", "updateUser", "deleteUser", "registerUser", "showRegister"};

        String[] productActions = {"viewProducts", "addProduct", "deleteProduct",
            "viewProductDetail", "filterProduct",
            "callSaveProduct", "addProductWithVariant", "updateProductWithVariant", "deleteProductWithVariant", "toggleProductStatus"};

        String[] brandActions = {"viewBrandList", "updateBrand", "addBrand", "filterBrand", "callBrandForm"};
        String[] categoryActions = {"viewCategoryList", "updateCategory", "addCategory", "callCategoryForm", "filterCategory", "deleteCategory"};

        if (txtAction != null) {
            if (Arrays.asList(userActions).contains(txtAction)) {
                url = "UserController";
            } else if (Arrays.asList(productActions).contains(txtAction)) {
                url = "ProductController";
            } else if (Arrays.asList(categoryActions).contains(txtAction)) {
                url = "CategoryController";
            } else if (Arrays.asList(brandActions).contains(txtAction)) {
                url = "BrandController";
            }
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
