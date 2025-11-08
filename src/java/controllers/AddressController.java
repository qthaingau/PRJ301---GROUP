// src/java/controllers/AddressController.java
package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.AddressDAO;
import models.AddressDTO;

public class AddressController extends HttpServlet {

    private final AddressDAO dao = new AddressDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("txtAction");
        HttpSession session = req.getSession();
        String userID = (String) session.getAttribute("userID");

        if (userID == null) {
            resp.sendRedirect("MainController?txtAction=login");
            return;
        }

        try {
            if (action == null) {
                action = "";
            }

            switch (action) {
                case "viewAddressList":
                    viewList(req, resp, userID);
                    break;
                case "showAddAddressForm":
                    req.getRequestDispatcher("addAddress.jsp").forward(req, resp);
                    break;
                case "addAddress":
                    addAddress(req, resp, userID);
                    break;
                case "deleteAddress":
                    deleteAddress(req, resp, userID);
                    break;
                case "setDefaultAddress":
                    setDefault(req, resp, userID);
                    break;
                default:
                    viewList(req, resp, userID);
                    break;
            }
        } catch (SQLException e) {
            req.setAttribute("msg", "Lỗi: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private void viewList(HttpServletRequest req, HttpServletResponse resp, String userID) throws SQLException, ServletException, IOException {
        List<AddressDTO> list = dao.getAddressesByUser(userID);
        req.setAttribute("addresses", list);
        req.setAttribute("returnUrl", req.getParameter("returnUrl"));
        req.getRequestDispatcher("addressList.jsp").forward(req, resp);
    }

    private void addAddress(HttpServletRequest req, HttpServletResponse resp, String userID)
            throws SQLException, IOException {
        AddressDTO a = new AddressDTO();
        a.setUserID(userID);
        a.setRecipientName(req.getParameter("recipientName"));
        a.setPhoneNumber(req.getParameter("phoneNumber"));
        a.setStreet(req.getParameter("street"));
        a.setWard(req.getParameter("ward"));
        a.setDistrict(req.getParameter("district"));
        a.setCity(req.getParameter("city"));
        a.setIsDefault("on".equals(req.getParameter("isDefault")));

        String newID = dao.addAddress(a);
        String returnUrl = req.getParameter("returnUrl");

        // Nếu không có returnUrl → mặc định về danh sách
        if (returnUrl == null || returnUrl.isEmpty()) {
            returnUrl = "MainController?txtAction=viewAddressList";
        }

        // Thêm tham số để chọn địa chỉ mới + thông báo
        String redirect = returnUrl
                + (returnUrl.contains("?") ? "&" : "?")
                + "selectedAddress=" + newID
                + "&msg=Thêm+địa+chỉ+thành+công!";

        resp.sendRedirect(redirect);
    }

    private void deleteAddress(HttpServletRequest req, HttpServletResponse resp, String userID)
            throws SQLException, IOException {
        String id = req.getParameter("addressID");
        boolean ok = dao.deleteAddress(id, userID);
        resp.sendRedirect("MainController?txtAction=viewAddressList&msg=" + (ok ? "Xóa thành công!" : "Không thể xóa!"));
    }

    private void setDefault(HttpServletRequest req, HttpServletResponse resp, String userID)
            throws SQLException, IOException {
        String id = req.getParameter("addressID");
        dao.setDefault(id, userID);
        resp.sendRedirect("MainController?txtAction=viewAddressList&msg=Đặt mặc định thành công!");
    }
}
