package Data;

import Application.Product;
import Application.User;
import Application.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addProduct(String name, double cost, String category, int quantity) {
        String addProductQuery =
                "INSERT INTO Product (NAME, COST, CATEGORY, QUANTITY) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(addProductQuery);
            stmt.setString(1, name);
            stmt.setDouble(2, cost);
            stmt.setString(3, category);
            stmt.setInt(4, quantity);
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
        String getProductQuery =
                "SELECT ID, NAME, COST,CATEGORY, QUANTITY FROM PRODUCT WHERE PRODUCT.ID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(getProductQuery);
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

    private Product mapProduct(ResultSet queryResult) throws SQLException {
        return new Product(
                queryResult.getInt("ID"),
                queryResult.getString("NAME"),
                queryResult.getDouble("COST"),
                queryResult.getString("CATEGORY"),
                queryResult.getInt("QUANTITY")
        );
    }
}
