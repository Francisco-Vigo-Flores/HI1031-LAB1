package Data.Dao;
import Application.Entities.CartProduct;
import Application.Entities.Product;
import Data.ShopDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class CartDAO {
    private final ShopDB shopDB;

    public CartDAO() {
            this.shopDB = new ShopDB();
    }

    public boolean addCart(int userID) {
        String addCartQuery = "INSERT INTO Cart (UserID) VALUES (?)";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(addCartQuery)) {
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

    public boolean addProduct(int productID, int cartID, int quantity) {
        String addSameProductsQuery = "INSERT INTO CartProducts (CartID, ProductID, Quantity) " +
                "SELECT ?, ID, ? FROM Product WHERE ID = ? AND Quantity >= ? " +
                "ON CONFLICT (CartID, ProductID) DO UPDATE SET Quantity = CartProducts.Quantity+EXCLUDED.Quantity " +
                "WHERE CartProducts.Quantity <= " +
                "(SELECT Quantity FROM Product WHERE ID = EXCLUDED.ProductID) - EXCLUDED.Quantity";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(addSameProductsQuery)) {
            stmt.setInt(1, cartID);
            stmt.setInt(2, quantity);
            stmt.setInt(3, productID);
            stmt.setInt(4, quantity);
            if (stmt.executeUpdate() == 1) {
                System.out.println("Successfully added " + quantity + "to cart");
                return true;
            }
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            throw new IllegalStateException("Could not add product to cart", e);
        }
        return false;
    }

    public boolean addOneProduct(int productID, int cartID) {
        return addProduct(productID,cartID,1);
    }

    public boolean removeProduct(int productID, int cartID, int quantity) {
        String removeProductQuery = "UPDATE CartProducts SET Quantity = Quantity - ? " +
                "WHERE CartID = ? AND ProductID = ? AND Quantity >= ?";
        String deleteEmptyItemQuery = "DELETE FROM CartProducts " +
                "WHERE CartID = ? AND ProductID = ? AND Quantity = 0";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(removeProductQuery)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartID);
            stmt.setInt(3, productID);
            stmt.setInt(4, quantity);
            if (stmt.executeUpdate() != 1) {
                return false;
            }

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteEmptyItemQuery)) {
                deleteStmt.setInt(1, cartID);
                deleteStmt.setInt(2, productID);
                deleteStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Error when removing product");
        }
        return false;
    }

    public boolean removeOneProduct(int productID, int cartID) {
        return removeProduct(productID, cartID, 1);
    }

    public boolean clearCart(int cartID) {
        String clearCartQuery = "DELETE FROM CartProducts WHERE CartID = ?";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(clearCartQuery)) {
            stmt.setInt(1, cartID);
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not clear cart");
        }
        return false;
    }

    public int getCartIdByUserId(int userID) {
        String getCartQuery = "SELECT ID FROM Cart WHERE UserID = ?";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(getCartQuery)) {
            stmt.setInt(1, userID);

            ResultSet result = stmt.executeQuery();
            if (!result.next()) {
                System.out.println("Could not find cart for userID:" + userID);
                return -1;
            }
            return result.getInt("ID");
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not find cart for user");
        }
        return -1;
    }

    public ArrayList<CartProduct> getCartProducts(int cartID) {
        ArrayList<CartProduct> cartProducts = new ArrayList<>();
        String getCartProductsQuery = "SELECT p.ID, p.NAME, p.COST AS \"Cost per product\", " +
                "p.CATEGORY, p.DESCRIPTION, p.QUANTITY AS \"Amount in stock\", cp.QUANTITY AS \"Amount in cart\" " +
                "FROM Product as p JOIN CartProducts as cp " +
                "ON cp.ProductID = p.ID WHERE cp.CartID = ?";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(getCartProductsQuery)) {
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
