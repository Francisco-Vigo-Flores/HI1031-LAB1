package Presentation;

import Application.ShopService;
import Application.Dto.ProductDTO;
import Application.Dto.UserDTO;
import Application.UserType;
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
        UserDTO user = requireStaff(request, response);
        if (user == null) return;
        String id = request.getParameter("productId");
        if (id != null && request.getAttribute("error") == null) {
            try {
                ProductDTO product = shopService.getProduct(Integer.parseInt(id));
                if (product == null) {
                    response.sendRedirect(request.getContextPath() + "/inventory");
                    return;
                }
                request.setAttribute("product", product);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/inventory");
                return;
            }
        }
        request.setAttribute("categories", shopService.getCategories());
        request.setAttribute("currentPage", "/inventory");
        request.setAttribute("cartCount", shopService.getCart(user.getId()).getProductCount());
        request.setAttribute("products", shopService.getAllProducts());
        request.getRequestDispatcher("/WEB-INF/Views/inventory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDTO user = requireStaff(request, response);
        if (user == null) return;
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        boolean saved = false;
        try {
            if ("category".equals(action)) {
                saved = shopService.saveCategory(request.getParameter("oldName"), request.getParameter("name"));
            } else if ("addProduct".equals(action)) {
                saved = shopService.addProduct(request.getParameter("name"),
                        Double.parseDouble(request.getParameter("cost")), request.getParameter("category"),
                        request.getParameter("desc"), Integer.parseInt(request.getParameter("quantity")));
            } else if ("updateProduct".equals(action)) {
                saved = shopService.updateProduct(Integer.parseInt(request.getParameter("id")),
                        request.getParameter("name"), Double.parseDouble(request.getParameter("cost")),
                        request.getParameter("category"), request.getParameter("desc"),
                        Integer.parseInt(request.getParameter("quantity")));
            } else if (action == null || "stock".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                saved = shopService.updateStock(productId, quantity);
            }
        } catch (NumberFormatException e) {
            saved = false;
        }
        if (saved) {
            response.sendRedirect(request.getContextPath() + "/inventory?updated=true");
            return;
        }
        request.setAttribute("error", "Kunde inte spara. Kontrollera fälten och försök igen.");
        doGet(request, response);
    }

    private UserDTO requireStaff(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserDTO user = session == null ? null : (UserDTO) session.getAttribute("user");
        if (user != null && (user.getType() == UserType.Admin || user.getType() == UserType.InventoryManager)) {
            return user;
        }
        response.sendRedirect(request.getContextPath() + (user == null ? "/login" : "/products"));
        return null;
    }
}
