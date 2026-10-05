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

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Cart cart = shopService.getCart(user.getId());
        request.setAttribute("currentPage", "/cart");
        request.setAttribute("cartCount", cart.getProductCount());
        request.setAttribute("cartItems", cart.getProducts());
        request.setAttribute("cartTotal", cart.calculateTotalCost());
        request.getRequestDispatcher("/WEB-INF/Views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "add";
        try {
            switch (action) {
                case "checkout":
                    int orderId = shopService.placeOrder(user.getId());
                    response.sendRedirect(request.getContextPath() +
                            (orderId == -1 ? "/cart?checkout=failed" : "/orders?placed=" + orderId));
                    return;
                case "clear":
                    shopService.clearCart(user.getId());
                    break;
                case "remove":
                    shopService.removeFromCart(user.getId(),
                            Integer.parseInt(request.getParameter("productId")), 1);
                    break;
                case "add":
                    boolean added = shopService.addToCart(user.getId(),
                            Integer.parseInt(request.getParameter("productId")));
                    if (!added) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        return;
                    }
                    if ("fetch".equals(request.getHeader("X-Requested-With"))) {
                        response.setHeader("X-Cart-Count",
                                String.valueOf(shopService.getCart(user.getId()).getProductCount()));
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/products");
                    return;
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
