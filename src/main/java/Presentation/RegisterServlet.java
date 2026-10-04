package Presentation;

import Application.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final ShopService shopService = new ShopService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("registering", true);
        req.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!shopService.register(req.getParameter("username"), req.getParameter("name"), req.getParameter("password"))) {
            req.setAttribute("error", "Kunde inte skapa kontot. Kontrollera uppgifterna eller prova ett annat anvndarnamn.");
            doGet(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/login?registered=true");
    }
}
