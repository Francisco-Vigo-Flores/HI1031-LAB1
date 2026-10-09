package Application;

import Application.Entities.*;
import Application.Dto.*;
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

    public UserDTO login(String username, String password) {
        int userID = userDAO.userExists(username,password);
        if(userID==-1) {
            return null;
        }
        return toUserDTO(userDAO.getUser(userID));
    }

    public boolean register(String username, String name, String password) {
        return register(username, name, password, UserType.Customer);
    }

    public boolean register(String username, String name, String password, UserType type) {
        if (username == null || name == null || password == null || type == null) {
            return false;
        }
        if (username.isEmpty() || username.length() > 50 || name.isEmpty() || name.length() > 50
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

    public UserDTO getUser(int userID) {
        return toUserDTO(userDAO.getUser(userID));
    }

    public List<UserDTO> getAllUsers() {
        return toUserDTOList(userDAO.getAllUsers());
    }

    public boolean updateUser(int userID, String name, UserType type) {
        if (name == null || name.trim().isEmpty() || name.length() > 50 ) {
            return false;
        }
        return userDAO.updateUser(userID, name, type);
    }

    public boolean deleteUser(int userID) {
        return userDAO.deleteUser(userID);
    }

    public List<ProductDTO> getAllProducts() {
        return toProductDTOList(productDAO.getAllProducts());
    }

    public ProductDTO getProduct(int productID) {
        return toProductDTO(productDAO.getProduct(productID));
    }

    public boolean updateStock(int productID, int quantity) {
        if (productID <= 0 || quantity < 0) {
            return false;
        }
        return productDAO.updateStock(productID, quantity);
    }

    public List<String> getCategories() {
        return productDAO.getCategories();
    }

    public boolean saveCategory(String oldName, String name) {
        if (oldName == null || oldName.trim().isEmpty() || !hasText(name, 50)) {
            return false;
        }
        return productDAO.saveCategory(oldName, name.trim());
    }

    public boolean addProduct(String name, double cost, String category, String desc, int quantity) {
        if (!validProductDetails(name, cost, category, desc, quantity)) {
            return false;
        }
        return productDAO.addProduct(name.trim(), cost, category.trim(), desc.trim(), quantity);
    }

    public boolean updateProduct(int id, String name, double cost, String category, String desc, int quantity) {
        if (id <= 0 || !validProductDetails(name, cost, category, desc, quantity)) {
            return false;
        }
        return productDAO.updateProduct(id, name.trim(), cost, category.trim(), desc.trim(), quantity);
    }

    private boolean validProductDetails(String name, double cost, String category, String desc, int quantity) {
        return cost > 0 && quantity >= 0 && hasText(name, 50) && hasText(category, 50) && hasText(desc, 100);
    }

    private boolean hasText(String value, int maxLength) {
        return value != null && !value.trim().isEmpty() && value.trim().length() <= maxLength;
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
        Product product = productDAO.getProduct(productID);
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

    public CartDTO getCart(int userID) {
        return toCartDTO(loadCart(userID));
    }

    private Cart loadCart(int userID) {
        int cartID = getOrCreateCartId(userID);
        ArrayList<CartProduct> products = cartDAO.getCartProducts(cartID);
        return new Cart(products);
    }

    public int placeOrder(int userID) {
        User user = userDAO.getUser(userID);
        Cart cart = loadCart(userID);
        if (cart.getProducts().isEmpty()) {
            return -1;
        }
        int orderId = orderDAO.order(user, cart);
        if (orderId != -1) {
            clearCart(userID);
        }
        return orderId;
    }

    public List<CartProductDTO> getOrderProducts(int orderID) {
        return toCartProductDTOList(orderDAO.getOrderProducts(orderID));
    }

    public boolean completeOrder(int orderID) {
        return orderDAO.completeOrder(orderID);
    }

    public List<OrderDTO> getOrdersByUserId(int userID) {
        return toOrderDTOList(orderDAO.getOrdersByUserId(userID));
    }

    public List<OrderDTO> getOrders() {
        return toOrderDTOList(orderDAO.getOrders());
    }

    private UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getType(), user.getName(), user.getUsername());
    }

    private ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDTO(product.getId(), product.getName(), product.getCost(),
                product.getCategory(), product.getDesc(), product.getQuantity());
    }

    private CartProductDTO toCartProductDTO(CartProduct item) {
        return new CartProductDTO(item.getAmountInCart(), toProductDTO(item.getProduct()),
                item.getProductCost());
    }

    private CartDTO toCartDTO(Cart cart) {
        return new CartDTO(toCartProductDTOList(cart.getProducts()),
                cart.getProductCount(), cart.calculateTotalCost());
    }

    private OrderDTO toOrderDTO(Order order) {
        return new OrderDTO(order.getId(), order.isComplete(), toUserDTO(order.getUser()),
                toCartProductDTOList(order.getProducts()));
    }

    // Preserve the DAOs' distinction between load failure (null) and no results.
    private List<UserDTO> toUserDTOList(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toUserDTO(user));
        }
        return dtos;
    }

    private List<ProductDTO> toProductDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }
        List<ProductDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(toProductDTO(product));
        }
        return dtos;
    }

    private List<CartProductDTO> toCartProductDTOList(List<CartProduct> items) {
        if (items == null) {
            return null;
        }
        List<CartProductDTO> dtos = new ArrayList<>();
        for (CartProduct item : items) {
            dtos.add(toCartProductDTO(item));
        }
        return dtos;
    }

    private List<OrderDTO> toOrderDTOList(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        List<OrderDTO> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(toOrderDTO(order));
        }
        return dtos;
    }
}
