package controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.BrandDAO;
import models.BrandDTO;

@WebServlet(name = "BrandController", urlPatterns = {"/BrandController"})
public class BrandController extends HttpServlet {

    // Lấy danh sách brand active để menu header
    protected void loadActiveBrands(HttpServletRequest request) throws Exception {
        BrandDAO dao = new BrandDAO();
        List<BrandDTO> listBrand = dao.getActiveBrands(); // chỉ active = 1
        request.setAttribute("listBrand", listBrand);
    }

    // Hiển thị tất cả brand (quản lý)
    protected void viewBrandList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            BrandDAO dao = new BrandDAO();
            List<BrandDTO> listBrands = dao.getAllBrand();
            request.setAttribute("brandList", listBrands);
            request.getRequestDispatcher("customer/brandList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error loading brand list!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Lọc brand theo keyword
    protected void filterBrand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            BrandDAO dao = new BrandDAO();
            List<BrandDTO> list;

            if (keyword == null || keyword.trim().isEmpty()) {
                list = dao.getAllBrand();
            } else {
                list = dao.filterBrand(keyword);
            }

            request.setAttribute("brandList", list);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("customer/brandList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error filtering brand!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Xử lý request
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            // Luôn load brand active cho menu header
            loadActiveBrands(request);

            String action = request.getParameter("txtAction");
            if ("viewBrandList".equals(action)) {
                viewBrandList(request, response);
            } else if ("filterBrand".equals(action)) {
                filterBrand(request, response);
            } else {
                // Mặc định forward về home.jsp
                request.getRequestDispatcher("home.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "Error processing brand request!");
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
        return "BrandController - xử lý brand riêng";
    }
}
