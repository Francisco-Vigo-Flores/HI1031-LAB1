package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ShopDB {
    private Connection connection;
    private final String databaseURL;
    private final String databaseUser;
    private final String databasePassword;

    public ShopDB(String databaseURL, String databaseUser, String databasePassword) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    public void Connect() {
        try {
            Connection connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
            System.out.println("Connection successful");
            this.connection = connection;
        }
        catch (SQLException exception){
            System.out.println("Connection failed");
            printErrors(exception);
        }
    }

    public void disconnect() {
        try {
            connection.close();
        }
        catch (SQLException exception) {
            System.out.println("Disconnect failed");
            printErrors(exception);
        }
    }

    public static void printErrors(SQLException exception) {
        System.out.println("Error code: " + exception.getErrorCode());
        System.out.println("Error msg: " + exception.getMessage());
        System.out.println("Error cause: " + exception.getCause());
    }

    public Connection getConnection() {
        return connection;
    }
}
