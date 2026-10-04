package Presentation;

import Application.Entities.Cart;
import Application.Entities.User;
import Application.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/cart"})
public class CartServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Cart cart = shopService.getCart(user.getId());
        req.setAttribute("cart", cart);
        req.setAttribute("cartTotal", cart.calculateTotalCost());
        req.setAttribute("currentUser", user);
        req.setAttribute("currentPage", "/cart");
        req.setAttribute("cartCount", cart.getProductCount());
        req.getRequestDispatcher("/WEB-INF/Views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user;
        if (session == null) {
            user = null;
        } else {
            user = (User) session.getAttribute("user");
        }
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if ("clear".equals(req.getParameter("action"))) {
            shopService.clearCart(user.getId());
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        int productId = Integer.parseInt(req.getParameter("productId"));
        shopService.addToCart(user.getId(), productId);
        Cart cart = shopService.getCart(user.getId());
        resp.setHeader("X-Cart-Count", String.valueOf(cart.getProductCount()));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
