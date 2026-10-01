package Presentation;

import Application.Product;
import Data.ProductDAO;
import Data.ShopDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/products")
public class ProductsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ShopDB db = new ShopDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "0303");
        db.Connect();

        try {
            ProductDAO productDAO = new ProductDAO(db.getConnection());
            ArrayList<Product> products = productDAO.getAllProducts();

            request.setAttribute("products", products);
        } finally {
            db.disconnect();
        }
        request.getRequestDispatcher("/WEB-INF/Views/product.jsp").forward(request, response);
    }
}
