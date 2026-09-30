package Application;


// for login
public class User {
    private int id;
    UserType type;
    String name;
    String username;
    String password; //likely to change
    UserCart cart;

    public User(int id, UserType type, String name, String username) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.username = username;
    }
}
