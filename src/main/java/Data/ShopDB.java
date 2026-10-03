package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ShopDB {
    private final String databaseURL;
    private final String databaseUser;
    private final String databasePassword;

    public ShopDB(String databaseURL, String databaseUser, String databasePassword) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
            System.out.println("Connection successful");
            return connection;
        }
        catch (SQLException exception){
            System.out.println("Connection failed");
            printSqlErrors(exception);
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find PostgreSQL driver");
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void printSqlErrors(SQLException exception) {
        System.out.println("Error code: " + exception.getErrorCode());
        System.out.println("Error msg: " + exception.getMessage());
        System.out.println("Error cause: " + exception.getCause());
    }

}
