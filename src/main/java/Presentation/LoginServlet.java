package Presentation;

import Data.ShopDB;
import Data.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet({"", "/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showLogin(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("csrfToken");
        if (token == null || !token.equals(request.getParameter("csrfToken"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("error", "Sidan har gått ut. Ladda om och försök igen.");
            showLogin(request, response);
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || username.trim().isEmpty() || username.length() > 50
                || password == null || password.isEmpty() || password.length() > 256) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("error", "Ange användarnamn och lösenord.");
            showLogin(request, response);
            return;
        }
        ShopDB db = new ShopDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "0303");
        db.Connect();
        if (db.getConnection() == null) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return;
        }
        try {
            if (!new UserDAO(db.getConnection()).userExists(username, password)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("error", "Fel användarnamn eller lösenord.");
                showLogin(request, response);
                return;
            }
            request.changeSessionId();
            session.setAttribute("username", username);
            session.removeAttribute("csrfToken");
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect(request.getContextPath() + "/products");
        } finally {
            db.disconnect();
        }
    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }
        request.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(request, response);
    }
}
