package Application.Entities;

public class Product {
    private int id;
    private final String name;
    private final double cost;
    private final String category;
    private final String desc;
    int quantity;

    public Product(int id, String name, double cost, String category, String desc, int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.desc = desc;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
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

    public String getDesc() {
        return desc;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", category='" + category + '\'' +
                ", desc='" + desc + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
