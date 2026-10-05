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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("currentPage", "/profile");
        request.setAttribute("cartCount", shopService.getCart(user.getId()).getProductCount());
        request.setAttribute("profileUser", shopService.getUser(user.getId()));
        if (user.getType() == UserType.Admin) {
            request.setAttribute("users", shopService.getAllUsers());
        }
        request.getRequestDispatcher("/WEB-INF/Views/management.jsp").forward(request, response);
    }
}
