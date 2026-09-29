package Application;

import java.util.UUID;

// for login
public class User {
    private UUID id;
    UserType type;
    String name;
    String username;
    String password; //likely to change
    UserCart cart;
}
