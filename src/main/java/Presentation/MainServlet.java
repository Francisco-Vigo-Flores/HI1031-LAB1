package Presentation;

import Application.Product;
import Data.ProductDAO;
import Data.ShopDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet({"", "/cart"})
public class MainServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId;
        try {
            productId = Integer.parseInt(request.getParameter("productId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ShopDB db = new ShopDB("jdbc:postgresql://localhost:5432/HI1031-LAB1", "postgres", "password");
        db.Connect();
        if (db.getConnection() == null) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }
        Product product;
        try {
            product = new ProductDAO(db.getConnection()).getProduct(productId);
        } finally {
            db.disconnect();
        }
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ArrayList<Product> cart = (ArrayList<Product>) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            request.getSession().setAttribute("cart", cart);
        }
        cart.add(product);
        if ("fetch".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if ("/cart".equals(request.getServletPath())) {
            request.getRequestDispatcher("/WEB-INF/Views/cart.jsp").forward(request, response);
            return;
        }

        ShopDB db = new ShopDB("jdbc:postgresql://localhost:5432/HI1031-LAB1", "postgres", "password");
        db.Connect();

        try {
            ProductDAO productDAO = new ProductDAO(db.getConnection());
            ArrayList<Product> products = productDAO.getAllProducts();
            request.setAttribute("products", products);
        } finally {
            db.disconnect();
        }
        request.getRequestDispatcher("/WEB-INF/Views/main.jsp").forward(request, response);
    }
}
