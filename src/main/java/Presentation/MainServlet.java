package Presentation;
import Application.Entities.Product;
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
import java.util.List;

@WebServlet({"", "/products"})
public class MainServlet extends HttpServlet {
    ShopService shopService = new ShopService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = this.shopService.getAllProducts();
        request.setAttribute("products", products);
        HttpSession session = request.getSession(false);
        User user;
        if (session == null) {
            user = null;
        }
        else {
            user = (User) session.getAttribute("user");
        }
        request.setAttribute("currentUser", user);
        request.setAttribute("currentPage", "/products");
        request.setAttribute("cartCount", 0);
        if (user != null) {
            Cart cart = shopService.getCart(user.getId());
            request.setAttribute("cart", cart);
            request.setAttribute("cartCount", cart.getProductCount());
        }
        request.getRequestDispatcher("/WEB-INF/Views/main.jsp").forward(request,response);
    }
}
