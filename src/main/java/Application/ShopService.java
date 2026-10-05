package Application;

import Application.Entities.*;
import Data.Dao.CartDAO;
import Data.Dao.OrderDAO;
import Data.Dao.ProductDAO;
import Data.Dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;
    private final OrderDAO orderDAO;

    public ShopService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
    }

    public User login(String username, String password) {
        int userID = userDAO.userExists(username,password);
        if(userID==-1) {
            return null;
        }
        return userDAO.getUser(userID);
    }

    public boolean register(String username, String name, String password) {
        return register(username, name, password, UserType.Customer);
    }

    public boolean register(String username, String name, String password, UserType type) {
        if (username == null || name == null || password == null || type == null) {
            return false;
        }
        if (username.isEmpty() || username.length() > 50
                || name.isEmpty() || name.length() > 50
                || password.trim().isEmpty() || password.length() > 4) {
            return false;
        }
        if (userDAO.usernameTaken(username)) {
            return false;
        }
        int userID = userDAO.addUser(username, name, password, type);
        if (userID == -1) {
            return false;
        }
        return cartDAO.addCart(userID);
    }

    public User getUser(int userID) {
        return userDAO.getUser(userID);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(int userID, String name, UserType type) {
        if (name == null || name.trim().isEmpty() || name.length() > 50 || type == null) {
            return false;
        }
        return userDAO.updateUser(userID, name, type);
    }

    public boolean deleteUser(int userID) {
        return userDAO.deleteUser(userID);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProduct(int productID) {
        return productDAO.getProduct(productID);
    }

    public boolean updateStock(int productID, int quantity) {
        return productDAO.updateStock(productID, quantity);
    }

    private int getOrCreateCartId(int userID) {
        int cartID = cartDAO.getCartIdByUserId(userID);
        if (cartID == -1) {
            cartDAO.addCart(userID);
            cartID = cartDAO.getCartIdByUserId(userID);
        }
        return cartID;
    }

    public boolean addToCart(int userID, int productID) {
        return addToCart(userID, productID, 1);
    }

    public boolean addToCart(int userID, int productID, int quantity) {
        Product product = getProduct(productID);
        if (product == null || quantity <= 0 || product.getQuantity() < quantity) {
            return false;
        }
        int cartID = getOrCreateCartId(userID);
        return cartDAO.addProduct(productID, cartID, quantity);
    }

    public boolean removeFromCart(int userID, int productID, int quantity) {
        int cartID = getOrCreateCartId(userID);
        return cartDAO.removeProduct(productID, cartID, quantity);
    }

    public boolean clearCart(int userID) {
        int cartID = getOrCreateCartId(userID);
        return cartDAO.clearCart(cartID);
    }

    public Cart getCart(int userID) {
        int cartID = getOrCreateCartId(userID);
        ArrayList<CartProduct> products = cartDAO.getCartProducts(cartID);
        return new Cart(products);
    }

    public int placeOrder(int userID) {
        User user = userDAO.getUser(userID);
        if (user == null) {
            return -1;
        }
        Cart cart = getCart(userID);
        if (cart.getProducts() == null || cart.getProducts().isEmpty()) {
            return -1;
        }
        return orderDAO.order(user, cart);
    }

    public ArrayList<CartProduct> getOrderProducts(int orderID) {
        return orderDAO.getOrderProducts(orderID);
    }

    public boolean completeOrder(int orderID) {
        return orderDAO.completeOrder(orderID);
    }

    public ArrayList<Order> getOrdersByUserId(int userID) {
        return orderDAO.getOrdersByUserId(userID);
    }

    public ArrayList<Order> getOrders() {
        return orderDAO.getOrders();
    }
}
