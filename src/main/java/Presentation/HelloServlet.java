package Presentation;

import java.io.*;

import Application.Product;
import Data.ProductDAO;
import Data.ShopDB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        String dbUrl = "jdbc:postgresql://localhost:5432/HI1031-LAB1";
        String dbUsr = "postgres";
        String dbPwd = "password"; //yes
        ShopDB dbTest = new ShopDB(dbUrl, dbUsr, dbPwd);
        dbTest.Connect();
        ProductDAO productDaoTest = new ProductDAO(dbTest.getConnection());
        message = productDaoTest.getProduct(1).toString();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}