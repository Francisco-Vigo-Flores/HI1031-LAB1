package Presentation;

import Application.Dto.UserDTO;
import Application.UserType;
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
        UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
        if (user == null) {
            response.sendError(401);
            return;
        }
        if (user.getType() != UserType.Admin && user.getType() != UserType.InventoryManager) {
            response.sendError(403);
            return;
        }
        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
            if (orderId <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(400);
            return;
        }
        boolean completed = shopService.completeOrder(orderId);
        response.sendRedirect(request.getContextPath() + "/orders?completed=" + completed);
    }
}
