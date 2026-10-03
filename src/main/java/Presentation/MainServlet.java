package Presentation;

import Application.Model.Product;
import Application.ShopService;
import Data.Dao.CartDAO;
import Data.Dao.ProductDAO;
import Data.Dao.UserDAO;
import Data.ShopDB;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet({""})
public class MainServlet extends HttpServlet {
    ShopService shopService = new ShopService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = this.shopService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/Views/main.jsp").forward(request,response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
}
