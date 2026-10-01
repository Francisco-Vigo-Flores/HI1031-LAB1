package Data;

import Application.Product;
import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addProduct(String name, double cost, String category, String desc, int quantity) {
        String addProductQuery =
                "INSERT INTO Product (NAME, COST, CATEGORY, DESCRIPTION, QUANTITY) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(addProductQuery)){
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
        try (PreparedStatement stmt = connection.prepareStatement(singleProductQuery)){
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

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        String allProductsQuery =
                "SELECT ID, NAME, COST,CATEGORY, QUANTITY, DESCRIPTION FROM PRODUCT";
        try (PreparedStatement stmt = connection.prepareStatement(allProductsQuery)){
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
