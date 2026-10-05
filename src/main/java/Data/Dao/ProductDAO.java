package Data.Dao;

import Application.Entities.Product;
import Data.ShopDB;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {
    private ShopDB shopDB;

    public ProductDAO() {
        this.shopDB = new ShopDB();
    }

    public boolean addProduct(String name, double cost, String category, String desc, int quantity) {
        String addProductQuery =
                "INSERT INTO Product (NAME, COST, CATEGORY, DESCRIPTION, QUANTITY) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(addProductQuery)){
            stmt.setString(1, name);
            stmt.setDouble(2, cost);
            stmt.setString(3, category);
            stmt.setString(4, desc);
            stmt.setInt(5, quantity);
            if(stmt.executeUpdate() == 1){
                System.out.println("Product added successfully");
                return true;
            }
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not add product to DB");
        }
        return false;
    }

    public Product getProduct(int productID) {
        String singleProductQuery =
                "SELECT ID, NAME, COST,CATEGORY, DESCRIPTION, QUANTITY FROM PRODUCT WHERE PRODUCT.ID = ?";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(singleProductQuery)){
            stmt.setInt(1, productID);
            ResultSet queryResults = stmt.executeQuery();
            if (!queryResults.next()) {
                System.out.println("Product not found");
                return null;
            }
            return mapProduct(queryResults);
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Query for single product failed");
        }
        return null;
    }

    public boolean updateProduct(int id, String name, double cost, String category, String desc, int quantity) {
        String sql = "UPDATE Product SET Name=?, Cost=?, Category=?, Description=?, Quantity=? WHERE ID=?";
        try (Connection connection = shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, cost);
            stmt.setString(3, category);
            stmt.setString(4, desc);
            stmt.setInt(5, quantity);
            stmt.setInt(6, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            return false;
        }
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        try (Connection connection = shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT Category FROM Product ORDER BY Category")) {
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                categories.add(results.getString("Category"));
            }
            return categories;
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            return null;
        }
    }

    public boolean saveCategory(String oldName, String name) {
        String sql = "UPDATE Product SET Category=? WHERE Category=?";
        try (Connection connection = shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, oldName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            return false;
        }
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String allProductsQuery =
                "SELECT ID, NAME, COST,CATEGORY, QUANTITY, DESCRIPTION FROM PRODUCT";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(allProductsQuery)){
            ResultSet queryResults = stmt.executeQuery();
            while(queryResults.next()) {
                products.add(mapProduct(queryResults));
            }
            return products;
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Query for all products failed");
        }
        return null;
    }


    public boolean removeProduct(int productID, int quantity) {
        try (Connection connection = this.shopDB.getConnection()) {
            return removeProduct(connection, productID, quantity);
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not decrease product stock");
        }
        return false;
    }

    protected boolean removeProduct(Connection connection, int productID, int quantity) throws SQLException {
        String removeProductQuery = "UPDATE Product SET Quantity = Quantity - ? " +
                "WHERE ID = ? AND Quantity >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(removeProductQuery)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);
            stmt.setInt(3, quantity);
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean removeOneProduct(int productID) {
        return removeProduct(productID,1);
    }

    public boolean updateStock(int productID, int quantity) {
        String updateStockQuery = "UPDATE Product SET Quantity = ? WHERE ID = ?";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateStockQuery)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productID);
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not update stock for product " + productID);
        }
        return false;
    }

    private Product mapProduct(ResultSet queryResult) throws SQLException {
        return new Product(
                queryResult.getInt("ID"),
                queryResult.getString("NAME"),
                queryResult.getDouble("COST"),
                queryResult.getString("CATEGORY"),
                queryResult.getString("DESCRIPTION"),
                queryResult.getInt("QUANTITY")
        );
    }
}
