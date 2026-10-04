package Application;

import Application.Entities.Cart;
import Application.Entities.CartProduct;
import Application.Entities.Product;
import Application.Entities.User;
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
        if (username == null || name == null || password == null) {
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
        int userID = userDAO.addUser(username, name, password);
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

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProduct(int productID) {
        return productDAO.getProduct(productID);
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
        if (cart.getProducts() == null) {
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

    public ArrayList<Integer> getOrderIdsByUserId(int userID) {
        return orderDAO.getOrderIdsByUserId(userID);
    }

    public ArrayList<Integer> getIncompleteOrderIds() {
        return orderDAO.getIncompleteOrderIds();
    }
}
