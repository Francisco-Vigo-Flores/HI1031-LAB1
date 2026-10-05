package Presentation;

import Application.ShopService;
import Application.Entities.User;
import Application.Entities.UserType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (user.getType() != UserType.Admin && user.getType() != UserType.InventoryManager) {
            response.sendError(403);
            return;
        }
        request.setAttribute("currentPage", "/inventory");
        request.setAttribute("cartCount", shopService.getCart(user.getId()).getProductCount());
        request.setAttribute("products", shopService.getAllProducts());
        request.getRequestDispatcher("/WEB-INF/Views/inventory.jsp").forward(request, response);
    }
}
