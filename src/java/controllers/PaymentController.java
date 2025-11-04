/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.PaymentDAO;
import models.PaymentDTO;

/**
 *
 * @author nguye
 */
@WebServlet(name = "PaymentController", urlPatterns = {"/PaymentController"})
public class PaymentController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Tạo đối tượng DAO và lấy danh sách payment
        PaymentDAO dao = new PaymentDAO();
        ArrayList<PaymentDTO> paymentList = dao.getAllPayment();

        // Gửi danh sách qua JSP để hiển thị
        request.setAttribute("paymentList", paymentList);

        // Chuyển hướng tới trang JSP
        request.getRequestDispatcher("listOfPayment.jsp").forward(request, response);
    }

    // GET: hiển thị danh sách
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // POST: tạm thời cũng gọi processRequest (sau này thêm chức năng add/update sẽ tách riêng)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị danh sách thanh toán";
    }
}
