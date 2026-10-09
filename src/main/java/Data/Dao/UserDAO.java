package Data.Dao;

import Application.Entities.User;
import Application.UserType;
import Data.ShopDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {
    private final ShopDB shopDB;

    public UserDAO() {
        this.shopDB = new ShopDB();
    }
    public int addUser(String username, String name, String password){
        return addUser(username, name, password, UserType.Customer);
    }

    public int addUser(String username, String name, String password, UserType type) {
        String addUserQuery =
                "INSERT INTO \"User\" (NAME, USERNAME, PASSWORD, TYPE) VALUES (?, ?, ?, ?) RETURNING ID";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(addUserQuery)){
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setObject(4, type.name(), java.sql.Types.OTHER);
            ResultSet queryResults = stmt.executeQuery();
            if (queryResults.next()) {
                System.out.println("User added successfully");
                return queryResults.getInt("ID");
            }

        }
        catch (SQLException e) {
                ShopDB.printSqlErrors(e);
                System.out.println("Could not add user to DB");
        }
        return -1;
    }

    public boolean updateUser(int userID, String name, UserType type) {
        String updateUserQuery = "UPDATE \"User\" SET NAME = ?, TYPE = ? WHERE ID = ?";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateUserQuery)) {
            stmt.setString(1, name);
            stmt.setObject(2, type.name(), java.sql.Types.OTHER);
            stmt.setInt(3, userID);
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not update user " + userID);
        }
        return false;
    }

    public boolean deleteUser(int userID) {
        String deleteProducts = "DELETE FROM CartProducts WHERE CartID IN " +
                "(SELECT ID FROM Cart WHERE UserID = ?)";
        String deleteCart = "DELETE FROM Cart WHERE UserID = ?";
        String deleteUser = "DELETE FROM \"User\" WHERE ID = ?";
        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement productsStmt = connection.prepareStatement(deleteProducts);
             PreparedStatement cartStmt = connection.prepareStatement(deleteCart);
             PreparedStatement userStmt = connection.prepareStatement(deleteUser)) {
            productsStmt.setInt(1, userID);
            productsStmt.executeUpdate();
            cartStmt.setInt(1, userID);
            cartStmt.executeUpdate();
            userStmt.setInt(1, userID);
            return userStmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not delete user " + userID);
        }
        return false;
    }

    public User getUser(int userID) {
        String singleUserQuery =
                "SELECT usr.ID, usr.NAME, usr.USERNAME, usr.TYPE FROM \"User\" as usr WHERE usr.ID = ?";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(singleUserQuery)) {
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

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(allUsersQuery)){
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

    public int userExists(String username, String password) {
        String findUserQuery = "SELECT usr.ID, usr.USERNAME, usr.PASSWORD FROM \"User\" AS usr " +
                "WHERE usr.USERNAME = ? AND usr.PASSWORD = ?";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(findUserQuery)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet queryResults = stmt.executeQuery();
            if (!queryResults.next()) {
                System.out.println("User doesn't exist");
                return -1;
            }
            return queryResults.getInt("ID");
        } catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("Could not find User with those credentials");
        }
        return -1;
    }
    public boolean usernameTaken(String username) {
        String usernameTakenQuery = "SELECT usr.USERNAME FROM \"User\" AS usr WHERE usr.USERNAME = ?";

        try (Connection connection = this.shopDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(usernameTakenQuery)) {
            stmt.setString(1,username);
            ResultSet queryResults = stmt.executeQuery();
            return (queryResults.next());
        }
        catch (SQLException e) {
            ShopDB.printSqlErrors(e);
            System.out.println("could not check whether that username was taken");
        }
        return false;
    }
    static protected User mapUser(ResultSet queryResult) throws SQLException {
        return new User(
                queryResult.getInt("ID"),
                UserType.valueOf(queryResult.getString("TYPE")),
                queryResult.getString("NAME"),
                queryResult.getString("USERNAME")
        );
    }
}
