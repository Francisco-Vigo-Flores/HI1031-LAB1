package Application;

import java.util.UUID;

public class Product {
    private int id;
    private final String name;
    private final double cost;
    private final String category;
    int quantity;

    public Product(int id, String name, double cost, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.quantity = quantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }
}
