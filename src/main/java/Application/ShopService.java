package Application;

import Application.Model.Cart;
import Application.Model.CartProduct;
import Application.Model.Product;
import Application.Model.User;
import Data.Dao.CartDAO;
import Data.Dao.ProductDAO;
import Data.Dao.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public ShopService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
        this.userDAO = new UserDAO();
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
        return userDAO.addUser(username, name, password);
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

    public ArrayList<Product> addToCart(ArrayList<Product> cart, int productID) {
        if (cart == null) {
            cart = new ArrayList<>();
        }
        Product product = productDAO.getProduct(productID);
        if (product != null) {
            cart.add(product);
        }
        return cart;
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
        int cartID = getOrCreateCartId(userID);
        return cartDAO.addOneProduct(productID, cartID);
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
        if (products == null) {
            return null;
        }
        return new Cart(products);
    }
}
