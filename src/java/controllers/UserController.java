/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import models.UserDAO;
import models.UserDTO;

/**
 *
 * @author TEST
 */
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)

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
        response.sendRedirect("home.jsp");
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

    private void processChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String current = request.getParameter("currentPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        if (!newPass.equals(confirm)) {
            request.setAttribute("msg", "New passwords do not match!");
            request.getRequestDispatcher("customer/changePassword.jsp").forward(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        UserDTO dbUser = dao.getUserByUsername(user.getUsername());

        if (!dbUser.getPassword().equals(current)) {
            request.setAttribute("msg", "Current password is incorrect!");
            request.getRequestDispatcher("customer/changePassword.jsp").forward(request, response);
            return;
        }

        boolean updated = dao.updatePassword(user.getUsername(), newPass);

        if (updated) {
            user.setPassword(newPass);
            session.setAttribute("user", user);
            request.setAttribute("msg", "Password changed successfully!");
        } else {
            request.setAttribute("msg", "Failed to change password!");
        }

        request.getRequestDispatcher("customer/changePassword.jsp").forward(request, response);
    }

    private void processUploadAvatar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Part filePart = request.getPart("avatar");
        if (filePart != null && filePart.getSize() > 0) {
            try ( InputStream inputStream = filePart.getInputStream();  ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                byte[] data = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, bytesRead);
                }

                byte[] imageBytes = buffer.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                UserDAO dao = new UserDAO();
                boolean updated = dao.updateAvatar(user.getUsername(), base64Image);

                if (updated) {
                    user.setAvatar(base64Image);
                    session.setAttribute("user", user);
                    System.out.println(" Avatar updated successfully as Base64 (" + base64Image.length() + " chars)");
                } else {
                    System.out.println("Avatar update failed in DB!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect(request.getContextPath() + "/customer/profile.jsp");

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
        } else if (txtAction.equals("showRegister")) {
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else if (txtAction.equals("changePassword")) {
            processChangePassword(request, response);
        } else if (txtAction.equals("uploadAvatar")) {
            processUploadAvatar(request, response);
        } else if (txtAction.equals("searchUser")) {
            try {
                String name = request.getParameter("txtName");
                UserDAO dao = new UserDAO();
                List<UserDTO> list;

                if (name == null || name.trim().isEmpty()) {
                    list = dao.getAllUsers(); // nếu không nhập gì thì hiển thị toàn bộ
                } else {
                    list = dao.searchUsersByName(name.trim());
                }

                request.setAttribute("name", name);
                request.setAttribute("listOfUsers", list);
                request.getRequestDispatcher("admin/listOfUsers.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "Lỗi khi tải danh sách người dùng!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else if (txtAction.equals("deleteUser")) {
            try {
                String uid = request.getParameter("uid");
                UserDAO dao = new UserDAO();
                boolean result = dao.softDeleteUser(uid);

                if (result) {
                    request.setAttribute("msg", "Xóa người dùng thành công (đã chuyển sang Inactive).");
                } else {
                    request.setAttribute("msg", "Không thể xóa người dùng!");
                }

                // Sau khi xóa, load lại danh sách
                List<UserDTO> list = dao.getAllUsers();
                request.setAttribute("listOfUsers", list);
                request.getRequestDispatcher("admin/listOfUsers.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "Lỗi khi xóa người dùng!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else if (txtAction.equals("callUpdateUser")) {
            try {
                String uid = request.getParameter("uid");
                UserDAO dao = new UserDAO();
                UserDTO user = dao.getUserByID(uid); // phương thức cần thêm ở DAO
                request.setAttribute("userToEdit", user);
                request.getRequestDispatcher("customer/updateUser.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "Lỗi khi mở form cập nhật!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else if (txtAction.equals("updateUser")) {
            try {
                String userID = request.getParameter("userID");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phoneNumber = request.getParameter("phoneNumber");
                String role = request.getParameter("role");
                // checkbox hoặc select có thể trả null nếu không tick
                String activeParam = request.getParameter("active");
                boolean active = "on".equals(activeParam) || "1".equals(activeParam) || "true".equalsIgnoreCase(activeParam);

                UserDAO dao = new UserDAO();
                boolean updated = dao.updateUser(userID, fullName, email, phoneNumber, role, active);

                if (updated) {
                    request.setAttribute("msg", "Cập nhật người dùng thành công.");
                } else {
                    request.setAttribute("msg", "Cập nhật thất bại.");
                }

                // reload danh sách (chỉ users active nếu bạn muốn)
                List<UserDTO> list = dao.getAllUsers();
                request.setAttribute("listOfUsers", list);
                request.getRequestDispatcher("admin/listOfUsers.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("msg", "Lỗi khi cập nhật người dùng!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
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
