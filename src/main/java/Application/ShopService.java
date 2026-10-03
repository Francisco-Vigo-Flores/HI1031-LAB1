package Application;

import Application.Model.Cart;
import Application.Model.CartProduct;
import Application.Model.Product;
import Application.Model.User;
import Data.CartDAO;
import Data.ProductDAO;
import Data.UserDAO;

import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public ShopService(CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    public User login(String username, String password) {
        int userID = userDAO.userExists(username,password);
        if(userID==-1) {
            return null;
        }
        return userDAO.getUser(userID);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProduct(int productID) {
        return productDAO.getProduct(productID);
    }

    public ArrayList<Product> addToCart(ArrayList<Product> cart, int productID) {
        Product product = productDAO.getProduct(productID);
        if (product == null) {
            return null;
        }
        cart.add(product);
        return cart;
    }

    public Cart getCart(int userID) {
        int cartID = cartDAO.getCartIdByUserId(userID);
        if (cartID == -1) {
            return null;
        }
        ArrayList<CartProduct> products = cartDAO.getCartProducts(cartID);
        if (products == null) {
            return null;
        }
        return new Cart(products);
    }
}
