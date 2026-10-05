package Data.Dao;

import Application.Entities.User;
import Application.Entities.UserType;
import Data.ShopDB;

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

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(addUserQuery)){
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, type.name());
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
        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(updateUserQuery)) {
            stmt.setString(1, name);
            stmt.setString(2, type.name());
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
        try (PreparedStatement productsStmt = this.shopDB.getConnection().prepareStatement(deleteProducts);
             PreparedStatement cartStmt = this.shopDB.getConnection().prepareStatement(deleteCart);
             PreparedStatement userStmt = this.shopDB.getConnection().prepareStatement(deleteUser)) {
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

        try(PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(singleUserQuery)) {
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

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(allUsersQuery)){
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

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(findUserQuery)) {
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

        try (PreparedStatement stmt = this.shopDB.getConnection().prepareStatement(usernameTakenQuery)) {
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
    private User mapUser(ResultSet queryResult) throws SQLException {
        return new User(
                queryResult.getInt("ID"),
                UserType.valueOf(queryResult.getString("TYPE")),
                queryResult.getString("NAME"),
                queryResult.getString("USERNAME")
        );
    }
}
