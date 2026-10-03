package Presentation;

import Data.ShopDB;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"", "/cart"})
public class MainServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String dbUrl = "jdbc:postgresql://localhost:5432/HI1031-LAB1";
        String dbUsr = "Postgres";
        String dbPwd = "password";
        ShopDB shopDB = new ShopDB(dbUrl, dbUsr, dbPwd);
    }
}
