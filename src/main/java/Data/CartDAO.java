package Data;

import Application.CartProduct;
import Application.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDAO {
    private final Connection connection;

    public CartDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addCart(int userID) {
        String addCartQuery = "INSERT INTO Cart (UserID) VALUES (?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(addCartQuery)) {
            stmt.setInt(1, userID);
            if(stmt.executeUpdate() == 1) {
                System.out.println("Cart added successfully");
                return true;
            }
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not add Cart");
        }
        return false;
    }

    public boolean addProduct(int cartID, int productID) {
        String addProduct = "INSERT INTO CartProducts (CartID, ProductID) VALUES (?,?)";

        try (PreparedStatement stmt = this.connection.prepareStatement(addProduct)) {
            stmt.setInt(1, cartID);
            stmt.setInt(2, productID);
            if (stmt.executeUpdate() == 1) {
                System.out.println("Product successfully added to cart");
                return true;
            }
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not add product to cart");
        }
        return false;
    }

    public ArrayList<CartProduct> getCartProducts(int cartID) {
        ArrayList<CartProduct> cartProducts = new ArrayList<>();
        String getCartProductsQuery = "SELECT p.ID, p.NAME, p.COST AS \"Cost per product\", " +
                "p.CATEGORY, p.DESCRIPTION, p.QUANTITY AS \"Amount in stock\", cp.QUANTITY AS \"Amount in cart\", "+
                "(cp.QUANTITY * p.COST) as \"Total Cost\" FROM Product as p JOIN CartProducts as cp " +
                "ON cp.ProductID = p.ID WHERE cp.CartID = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(getCartProductsQuery)) {
            stmt.setInt(1, cartID);
            ResultSet queryResults = stmt.executeQuery();
            while (queryResults.next()) {
                cartProducts.add(mapCartProduct(queryResults));
            }
            return cartProducts;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not return cart products");
        }
        return null;
    }

    private CartProduct mapCartProduct(ResultSet queryResults) throws SQLException {
        Product product = new Product(
                queryResults.getInt("ID"),
                queryResults.getString("NAME"),
                queryResults.getDouble("Cost per product"),
                queryResults.getString("CATEGORY"),
                queryResults.getString("DESCRIPTION"),
                queryResults.getInt("Amount in stock")
        );
        return new CartProduct(queryResults.getInt("Amount in cart"), product);
    }
}
