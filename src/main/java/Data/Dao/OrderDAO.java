package Data.Dao;

import Application.Entities.*;
import Data.ShopDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class OrderDAO {
    private final ShopDB shopDB;
    private final ProductDAO productDAO;

    public OrderDAO() {
        this.shopDB = new ShopDB();
        this.productDAO = new ProductDAO();
    }

    public int order(User user, Cart cart) {
        try (Connection connection = this.shopDB.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int orderId = addOrder(connection, user);
                decreaseProductsStock(connection, cart);
                addOrderProducts(connection, cart, orderId);
                connection.commit();
                return orderId;
            }
            catch (SQLException e) {
                connection.rollback();
            }
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not complete order");
        }
        return -1;
    }

    private int addOrder(Connection connection, User user) throws SQLException {
        String addOrderQuery = "INSERT INTO Orders (UserID) VALUES (?) RETURNING ID";
        try (PreparedStatement stmt = connection.prepareStatement(addOrderQuery)) {
            stmt.setInt(1, user.getId());
            try (ResultSet queryResults = stmt.executeQuery()) {
                if (!queryResults.next()) {
                    throw new SQLException("Order insert did not return an ID");
                }
                return queryResults.getInt("ID");
            }
        }
    }

    private void decreaseProductsStock(Connection connection, Cart cart) throws SQLException {
        for (CartProduct cp : cart.getProducts()) {
            if (!productDAO.removeProduct(connection, cp.getProduct().getId(), cp.getAmountInCart())) {
                throw new SQLException("Insufficient stock for product " + cp.getProduct().getId());
            }
        }
    }

    private void addOrderProducts(Connection connection, Cart cart, int orderId) throws SQLException {
        String addOrderProductsQuery = "INSERT INTO OrderProducts (OrderID, ProductId, Quantity, UnitPrice) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(addOrderProductsQuery)) {
            for (CartProduct cp : cart.getProducts()) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, cp.getProduct().getId());
                stmt.setInt(3, cp.getAmountInCart());
                stmt.setDouble(4, cp.getProduct().getCost());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public ArrayList<CartProduct> getOrderProducts(int orderId) {
        String query = "SELECT p.ID, p.NAME, p.CATEGORY, p.DESCRIPTION, p.QUANTITY AS \"Amount in stock\", " +
                "op.Quantity AS \"Amount ordered\", op.UnitPrice " +
                "FROM OrderProducts op JOIN Product p ON p.ID = op.ProductID " +
                "WHERE op.OrderID = ?";
        ArrayList<CartProduct> products = new ArrayList<>();

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                Product product = new Product(
                        results.getInt("ID"),
                        results.getString("NAME"),
                        results.getDouble("UnitPrice"),
                        results.getString("CATEGORY"),
                        results.getString("DESCRIPTION"),
                        results.getInt("Amount in stock")
                );
                products.add(new CartProduct(results.getInt("Amount ordered"), product));
            }
            return products;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not load products for order " + orderId);
        }
        return null;
    }

    public boolean completeOrder(int orderId) {
        String completeOrderQuery = "UPDATE Orders SET IsComplete = TRUE WHERE ID = ?";

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(completeOrderQuery)) {
            stmt.setInt(1, orderId);
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not complete order " + orderId);
            return false;
        }
    }

    public ArrayList<Integer> getOrderIdsByUserId(int userId) {
        String query = "SELECT ID FROM Orders WHERE UserID = ?";
        ArrayList<Integer> orderIds = new ArrayList<>();

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                orderIds.add(results.getInt("ID"));
            }
            return orderIds;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not load orders for user " + userId);
            return null;
        }
    }

    public ArrayList<Integer> getIncompleteOrderIds() {
        String query = "SELECT ID FROM Orders WHERE IsComplete = FALSE";
        ArrayList<Integer> orderIds = new ArrayList<>();

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(query)) {
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                orderIds.add(results.getInt("ID"));
            }
            return orderIds;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not load orders");
            return null;
        }
    }
}
