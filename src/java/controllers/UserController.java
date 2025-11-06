/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.UserDAO;
import models.UserDTO;

/**
 *
 * @author TEST
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String txtUsername = request.getParameter("txtUsername");
        String txtPassword = request.getParameter("txtPassword");

        UserDAO userDAO = new UserDAO();

        boolean checkLogin = userDAO.checkLogin(txtUsername, txtPassword);
        UserDTO user = null;
        String msg = "";
        if (!checkLogin) {
            msg = "Username or password incorrect!";
            request.setAttribute("msg", msg);
            request.setAttribute("username", txtUsername);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            user = userDAO.getUserByUsername(txtUsername);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("MainController?txtAction=viewProducts");
        }
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate(); // Huy tat ca nhung cai dang co trong session
        response.sendRedirect("login.jsp");
    }

    private void processRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("txtNewUsername");
        String email = request.getParameter("txtEmail");
        String password = request.getParameter("txtNewPassword");
        String confirm = request.getParameter("txtConfirmPassword");
        String fullName = request.getParameter("txtFullName");
        String phone = request.getParameter("txtPhoneNumber");

        if (!password.equals(confirm)) {
            request.setAttribute("msg", "Mật khẩu nhập lại không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.registerUser(username, email, password, fullName, phone);

        if (success) {
            request.setAttribute("msg", "Đăng ký thành công! Mời bạn đăng nhập.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "Tên đăng nhập hoặc email đã tồn tại, hoặc lỗi hệ thống!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String txtAction = request.getParameter("txtAction");
        if (txtAction == null) {
            txtAction = "login";
        }
        if (txtAction.equals("login")) {
            processLogin(request, response);
        } else if (txtAction.equals("logout")) {
            processLogout(request, response);
        } else if (txtAction.equals("registerUser")) {
            processRegister(request, response);
        }else if(txtAction.equals("showRegister")) {
            request.getRequestDispatcher("register.jsp").forward(request, response);
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
