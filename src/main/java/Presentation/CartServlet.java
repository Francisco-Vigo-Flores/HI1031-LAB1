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
            response.sendError(401);
            return;
        }

        String action = request.getParameter("action");
        boolean changed;
        if ("clear".equals(action)) {
            changed = shopService.clearCart(user.getId());
        } else {
            if (action != null && !"add".equals(action) && !"remove".equals(action)) {
                response.sendError(400);
                return;
            }

            int productId;
            try {
                productId = Integer.parseInt(request.getParameter("productId"));
                if (productId <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                response.sendError(400);
                return;
            }

            if (shopService.getProduct(productId) == null) {
                response.sendError(404);
                return;
            }

            if ("remove".equals(action)) {
                changed = shopService.removeFromCart(user.getId(), productId, 1);
            } else {
                changed = shopService.addToCart(user.getId(), productId);
            }
        }

        if (!changed) {
            response.sendError(409);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
