// src/java/controllers/CartController.java
package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import models.CartDAO;
import models.CartItemDAO;
import models.CartItemDTO;
import models.UserDTO;

@WebServlet("/CartController")
public class CartController extends HttpServlet {
    private final CartDAO cartDAO = new CartDAO();
    private final CartItemDAO cartItemDAO = new CartItemDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("txtAction");
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String userID = user.getUserID();
        String cartID = cartDAO.getOrCreateCartID(userID);

        if ("viewCart".equals(action)) {
            handleViewCart(request, response, cartID, session);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("txtAction");
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            response.getWriter().write("LOGIN_REQUIRED");
            return;
        }

        String userID = user.getUserID();
        String cartID = cartDAO.getOrCreateCartID(userID);

        if (action == null) {
            response.getWriter().write("INVALID_ACTION");
            return;
        }

        switch (action) {
            case "addToCart":
                handleAddToCart(request, response, cartID, session);
                break;
            case "updateCart":
                handleUpdateCart(request, response, cartID, session);
                break;
            case "removeFromCart":
                handleRemoveFromCart(request, response, cartID, session);
                break;
            case "addToCartFirst":
                handleAddToCartFirst(request, response, cartID, session);
                break;
            default:
                response.getWriter().write("INVALID_ACTION");
                break;
        }
    }

    // ================== HANDLE METHODS ==================
    private void handleViewCart(HttpServletRequest request, HttpServletResponse response,
                                String cartID, HttpSession session)
            throws ServletException, IOException {
        List<CartItemDTO> cartItems = cartItemDAO.getCartItemsByCartID(cartID);
        session.setAttribute("cart", cartItems);
        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("customer/cart.jsp").forward(request, response);
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response,
                                 String cartID, HttpSession session)
            throws IOException {
        String variantID = request.getParameter("variantID");
        int quantity = parseIntSafely(request.getParameter("quantity"), 1);
        boolean success = cartItemDAO.addOrUpdateItem(cartID, variantID, quantity);
        response.getWriter().write(success ? "OK" : "OUT_OF_STOCK");
        if (success) {
            updateCartSession(session, cartID);
        }
    }

    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response,
                                  String cartID, HttpSession session)
            throws IOException {
        String variantID = request.getParameter("variantID");
        int quantity = parseIntSafely(request.getParameter("quantity"), 1);
        cartItemDAO.updateQuantity(cartID, variantID, quantity);
        updateCartSession(session, cartID);
        response.sendRedirect("MainController?txtAction=viewCart");
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response,
                                      String cartID, HttpSession session)
            throws IOException {
        String variantID = request.getParameter("variantID");
        cartItemDAO.removeItem(cartID, variantID);
        updateCartSession(session, cartID);
        response.sendRedirect("MainController?txtAction=viewCart");
    }

    private void handleAddToCartFirst(HttpServletRequest request, HttpServletResponse response,
                                      String cartID, HttpSession session)
            throws IOException {
        String productID = request.getParameter("productID");
        int quantity = parseIntSafely(request.getParameter("quantity"), 1);
        String variantID = cartItemDAO.getFirstAvailableVariant(productID);
        if (variantID == null) {
            response.getWriter().write("OUT_OF_STOCK");
            return;
        }
        boolean added = cartItemDAO.addOrUpdateItem(cartID, variantID, quantity);
        response.getWriter().write(added ? "OK" : "FAILED");
        if (added) {
            updateCartSession(session, cartID);
        }
    }

    // ================== UTILITY ==================
    private void updateCartSession(HttpSession session, String cartID) {
        List<CartItemDTO> updated = cartItemDAO.getCartItemsByCartID(cartID);
        session.setAttribute("cart", updated);
    }

    private int parseIntSafely(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}