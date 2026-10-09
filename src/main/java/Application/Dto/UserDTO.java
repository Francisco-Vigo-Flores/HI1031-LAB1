package Application.Dto;

import Application.UserType;

/** User details for login, profile, and account views. */
public final class UserDTO {
    private final int id;
    private final UserType type;
    private final String name;
    private final String username;

    public UserDTO(int id, UserType type, String name, String username) {
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

    public String getUsername() {
        return username;
    }
}
