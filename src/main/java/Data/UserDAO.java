package Data;

import Application.User;
import Application.UserType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addUser(String username, String name, String password){
        String addUserQuery =
                "INSERT INTO \"User\" (NAME, USERNAME, PASSWORD, TYPE)  VALUES (?, ?, ?, 'Customer')";

        try (PreparedStatement stmt = this.connection.prepareStatement(addUserQuery)){
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

        try(PreparedStatement stmt = this.connection.prepareStatement(singleUserQuery)) {
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

        try (PreparedStatement stmt = this.connection.prepareStatement(allUsersQuery)){
            ResultSet queryResults = stmt.executeQuery();
            while(queryResults.next()) {
                users.add(mapUser(queryResults));
            }
            return users;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Query for all users failed");
        }
        return null;
    }

    public boolean userExists(String username, String password) {
        String findUserQuery = "SELECT usr.USERNAME, usr.PASSWORD FROM \"User\" AS usr " +
                "WHERE usr.USERNAME = ? AND usr.PASSWORD = ?";

        try (PreparedStatement stmt = this.connection.prepareStatement(findUserQuery)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet queryResults = stmt.executeQuery();
            if (!queryResults.next()) {
                System.out.println("User doesn't exist");
                return false;
            }
            return true;
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not find User with those credentials");
        }
        return false;
    }

    public User authenticate(String username, String password) throws SQLException {
        String query = "SELECT ID, NAME, USERNAME, TYPE FROM \"User\" WHERE USERNAME = ? AND PASSWORD = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet results = stmt.executeQuery()) {
                return results.next() ? mapUser(results) : null;
            }
        }
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
