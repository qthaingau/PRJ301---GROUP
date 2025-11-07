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
import models.BrandDTO;

/**
 *
 * @author TEST
 */
@WebServlet(name = "BrandController", urlPatterns = {"/BrandController"})
public class BrandController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processViewBrandList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {

            try {
                BrandDAO brandDAO = new BrandDAO();
                List<BrandDTO> listBrands = brandDAO.getAllBrand();

                HttpSession session = request.getSession();
                session.setAttribute("brandList", listBrands);

                // nếu em muốn list brand trong brandForm.jsp:
                request.getRequestDispatcher("customer/brandList.jsp").forward(request, response);

                // nếu muốn show trong home.jsp thì sửa lại:
                // request.getRequestDispatcher("home.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "Error loading brand list!");
                request.getRequestDispatcher("customer/brandList.jsp").forward(request, response);
            }
        }
    }
    
    protected void processViewBrandNameList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String url = "error.jsp";

        try {
            BrandDAO brandDAO = new BrandDAO();

                // ✅ Lấy danh sách brand đang active
                List<BrandDTO> listBrand = brandDAO.getActiveBrands();

                // ✅ Gửi sang JSP (thường là header hoặc trang home)
                request.setAttribute("listBrand", listBrand);

                // ✅ Chuyển tiếp về trang home.jsp hoặc header.jspf được include
                url = "home.jsp";
 

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("txtAction");

        if (action.equals("viewBrandList")) {
            processViewBrandList(request, response);
        }else if (action.equals("viewBrandNameList")) {
            processViewBrandNameList(request, response);
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
