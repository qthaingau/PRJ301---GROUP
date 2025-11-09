// src/java/controllers/CartController.java
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
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
        
        System.out.println("=== CartController GET ===");
        String action = request.getParameter("txtAction");
        System.out.println("Action: " + action);
        
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            System.out.println("User not logged in, redirecting to login");
            response.sendRedirect("login.jsp");
            return;
        }

        String userID = user.getUserID();
        System.out.println("User ID: " + userID);
        
        String cartID = cartDAO.getOrCreateCartID(userID).getCartID();
        System.out.println("Cart ID: " + cartID);

        if ("viewCart".equals(action)) {
            handleViewCart(request, response, cartID, session);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("=== CartController POST ===");
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("txtAction");
        System.out.println("Action: " + action);
        
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) {
            System.out.println("User not logged in");
            // For AJAX requests
            if ("addToCart".equals(action) || "updateCart".equals(action) || "removeFromCart".equals(action)) {
                response.setContentType("text/plain");
                response.getWriter().write("LOGIN_REQUIRED");
                return;
            }
            // For form submissions
            response.sendRedirect("login.jsp");
            return;
        }

        String userID = user.getUserID();
        System.out.println("User ID: " + userID);
        
        String cartID = cartDAO.getOrCreateCartID(userID).getCartID();
        System.out.println("Cart ID: " + cartID);

        if (action == null) {
            System.out.println("No action specified");
            response.setContentType("text/plain");
            response.getWriter().write("INVALID_ACTION");
            return;
        }

        try {
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
                    System.out.println("Unknown action: " + action);
                    response.setContentType("text/plain");
                    response.getWriter().write("INVALID_ACTION");
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error in CartController:");
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("ERROR: " + e.getMessage());
        }
    }

    // ================== HANDLE METHODS ==================
    private void handleViewCart(HttpServletRequest request, HttpServletResponse response,
                                String cartID, HttpSession session)
            throws ServletException, IOException {
        
        System.out.println("=== handleViewCart ===");
        System.out.println("CartID: " + cartID);
        
        List<CartItemDTO> cartItems = cartItemDAO.getCartItemsByCartID(cartID);
        System.out.println("Cart items count: " + (cartItems != null ? cartItems.size() : "null"));
        
        if (cartItems != null) {
            for (CartItemDTO item : cartItems) {
                System.out.println("  - " + item.getProductName() + " (x" + item.getQuantity() + ")");
            }
        }
        
        session.setAttribute("cart", cartItems);
        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("customer/cart.jsp").forward(request, response);
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response,
                                 String cartID, HttpSession session)
            throws IOException {
        
        System.out.println("=== handleAddToCart ===");
        
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String variantID = request.getParameter("variantID");
        String quantityStr = request.getParameter("quantity");
        
        System.out.println("VariantID: " + variantID);
        System.out.println("Quantity: " + quantityStr);
        
        // Validation
        if (variantID == null || variantID.trim().isEmpty()) {
            System.out.println("Invalid variant ID");
            out.write("INVALID_VARIANT");
            return;
        }
        
        int quantity = parseIntSafely(quantityStr, 1);
        if (quantity <= 0) {
            System.out.println("Invalid quantity: " + quantity);
            out.write("INVALID_QUANTITY");
            return;
        }
        
        System.out.println("Adding to cart - CartID: " + cartID + ", VariantID: " + variantID + ", Qty: " + quantity);
        
        boolean success = cartItemDAO.addOrUpdateItem(cartID, variantID, quantity);
        
        System.out.println("Add result: " + success);
        
        if (success) {
            updateCartSession(session, cartID);
            System.out.println("Cart updated in session");
            out.write("OK");
        } else {
            System.out.println("Failed to add to cart (likely out of stock)");
            out.write("OUT_OF_STOCK");
        }
    }

    private void handleUpdateCart(HttpServletRequest request, HttpServletResponse response,
                                  String cartID, HttpSession session)
            throws IOException {
        
        System.out.println("=== handleUpdateCart ===");
        
        String variantID = request.getParameter("variantID");
        String quantityStr = request.getParameter("quantity");
        int quantity = parseIntSafely(quantityStr, 1);
        
        System.out.println("VariantID: " + variantID + ", New Quantity: " + quantity);
        
        if (quantity <= 0) {
            // If quantity is 0 or negative, remove the item
            System.out.println("Quantity <= 0, removing item");
            cartItemDAO.removeItem(cartID, variantID);
        } else {
            System.out.println("Updating quantity");
            cartItemDAO.updateQuantity(cartID, variantID, quantity);
        }
        
        updateCartSession(session, cartID);
        response.sendRedirect("MainController?txtAction=viewCart");
    }

    private void handleRemoveFromCart(HttpServletRequest request, HttpServletResponse response,
                                      String cartID, HttpSession session)
            throws IOException {
        
        System.out.println("=== handleRemoveFromCart ===");
        
        String variantID = request.getParameter("variantID");
        System.out.println("Removing VariantID: " + variantID);
        
        cartItemDAO.removeItem(cartID, variantID);
        updateCartSession(session, cartID);
        response.sendRedirect("MainController?txtAction=viewCart");
    }

    private void handleAddToCartFirst(HttpServletRequest request, HttpServletResponse response,
                                      String cartID, HttpSession session)
            throws IOException {
        
        System.out.println("=== handleAddToCartFirst ===");
        
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String productID = request.getParameter("productID");
        int quantity = parseIntSafely(request.getParameter("quantity"), 1);
        
        System.out.println("ProductID: " + productID + ", Quantity: " + quantity);
        
        if (productID == null || productID.trim().isEmpty()) {
            out.write("INVALID_PRODUCT");
            return;
        }
        
        String variantID = cartItemDAO.getFirstAvailableVariant(productID);
        System.out.println("First available variant: " + variantID);
        
        if (variantID == null) {
            out.write("OUT_OF_STOCK");
            return;
        }
        
        boolean added = cartItemDAO.addOrUpdateItem(cartID, variantID, quantity);
        
        if (added) {
            updateCartSession(session, cartID);
            out.write("OK");
        } else {
            out.write("FAILED");
        }
    }

    // ================== UTILITY ==================
    private void updateCartSession(HttpSession session, String cartID) {
        System.out.println("Updating cart session for cartID: " + cartID);
        List<CartItemDTO> updated = cartItemDAO.getCartItemsByCartID(cartID);
        System.out.println("Updated cart item count: " + (updated != null ? updated.size() : "null"));
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