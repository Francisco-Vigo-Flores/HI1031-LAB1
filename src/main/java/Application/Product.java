package Application;

import java.util.UUID;

public class Product {
    private UUID id;
    private final String name;
    private final double cost;
    private final String category;
    private boolean inStock;

    public Product(String name, double cost, String category) {
        this.name = name;
        this.cost = cost;
        this.category = category;
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
