package Application.Entities;


public class User {
    private int id;
    private UserType type;
    private String name;
    private String username;
    private String password;
    private Cart cart;

    public User(int id, UserType type, String name, String username) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public UserType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Cart getCart() {
        return cart;
    }
}
