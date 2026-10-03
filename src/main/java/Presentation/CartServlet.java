package Presentation;

import Application.Model.Product;
import Application.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet({"/cart"})
public class CartServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/Views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productId = Integer.parseInt(req.getParameter("productId"));
        HttpSession session = req.getSession();
        ArrayList<Product> cart = (ArrayList<Product>) session.getAttribute("cart");
        ArrayList<Product> updatedCart = shopService.addToCart(cart, productId);
        session.setAttribute("cart", updatedCart);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
