package Presentation;

import Application.Entities.User;
import Application.Entities.UserType;
import Application.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        boolean isStaff = user.getType() == UserType.Admin || user.getType() == UserType.InventoryManager;
        request.setAttribute("orders", isStaff ? shopService.getOrders() : shopService.getOrdersByUserId(user.getId()));
        request.setAttribute("currentPage", "/orders");
        request.setAttribute("cartCount", shopService.getCart(user.getId()).getProductCount());
        request.getRequestDispatcher("/WEB-INF/Views/orders.jsp").forward(request, response);
    }
}
