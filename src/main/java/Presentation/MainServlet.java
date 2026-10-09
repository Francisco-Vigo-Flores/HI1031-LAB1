package Presentation;
import Application.Dto.ProductDTO;
import Application.Dto.CartDTO;
import Application.Dto.UserDTO;
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
        List<ProductDTO> products = this.shopService.getAllProducts();
        request.setAttribute("products", products);
        HttpSession session = request.getSession(false);
        UserDTO user;
        if (session == null) {
            user = null;
        }
        else {
            user = (UserDTO) session.getAttribute("user");
        }
        request.setAttribute("currentUser", user);
        request.setAttribute("currentPage", "/products");
        request.setAttribute("cartCount", 0);
        if (user != null) {
            CartDTO cart = shopService.getCart(user.getId());
            request.setAttribute("cart", cart);
            request.setAttribute("cartCount", cart.getProductCount());
        }
        request.getRequestDispatcher("/WEB-INF/Views/main.jsp").forward(request,response);
    }
}
