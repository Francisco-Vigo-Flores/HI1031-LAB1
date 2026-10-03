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
