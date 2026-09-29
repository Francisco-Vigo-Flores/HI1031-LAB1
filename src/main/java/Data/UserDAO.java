package Data;

import Application.User;
import Application.UserType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addUser(String username, String name, String password){
        String addUserQuery =
                "INSERT INTO \"User\" (NAME, USERNAME, PASSWORD, TYPE)  VALUES (?, ?, ?, 'Customer')";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(addUserQuery);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            if(stmt.executeUpdate() == 1){
                System.out.println("User added successfully");
                return true;
            }
        }
        catch (SQLException e) {
                ShopDB.printSqlErrors(e);
                System.out.println("Could not add user to DB");
        }
        return false;
    }

    public User getUser(int userID) {
        String singleUserQuery =
                "SELECT usr.ID, usr.NAME, usr.USERNAME, usr.TYPE FROM \"User\" as usr WHERE usr.ID = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(singleUserQuery);
            stmt.setObject(1, userID);
            ResultSet queryResults = stmt.executeQuery();

            if (!queryResults.next()) {
                System.out.println("User not found");
                return null;
            }
            return mapUser(queryResults);
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Single user query failed");
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String allUsersQuery =
                "SELECT usr.ID, usr.NAME, usr.USERNAME, usr.TYPE FROM \"User\" as usr";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(allUsersQuery);
            ResultSet queryResults = stmt.executeQuery();
            while(queryResults.next()) {
                users.add(mapUser(queryResults));
            }
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Query for all users failed");
        }
        return null;
    }

    private User mapUser(ResultSet queryResult) throws SQLException {
        return new User(
                queryResult.getInt("ID"),
                UserType.valueOf(queryResult.getString("TYPE")),
                queryResult.getString("NAME"),
                queryResult.getString("USERNAME")
        );
    }
}
