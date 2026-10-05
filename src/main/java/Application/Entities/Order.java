package Application.Entities;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private boolean isComplete;
    private User user;
    private ArrayList<CartProduct> products;

    public Order(User user, List<CartProduct> products) {
        this.user = user;
        this.products = new ArrayList<>(products);
        this.isComplete = false;
    }

    public Order(int id, boolean isComplete, User user, List<CartProduct> products) {
        this.id = id;
        this.isComplete = isComplete;
        this.user = user;
        this.products = new ArrayList<>(products);
    }


    public int getId() {
        return id;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<CartProduct> getProducts() {
        return new ArrayList<>(products);
    }
}
