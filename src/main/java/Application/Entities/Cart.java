package Application.Entities;

import java.util.ArrayList;

public class Cart {
    private ArrayList<CartProduct> products;

    public Cart(ArrayList<CartProduct> products) {
        this.products = products;
    }

    public ArrayList<CartProduct> getProducts() {
        return products;
    }

    public int getProductCount() {
        int count = 0;
        for (CartProduct product : products) {
            count += product.getAmountInCart();
        }
        return count;
    }

    public void removeAllOfItem(int productID){
        for (CartProduct cp: products){
            if(cp.getProduct().getId() == productID){
                this.products.remove(cp);
            }
        }
    }

    public void clearAllItems() {
        this.products.clear();
    }

    public double calculateTotalCost() {
        double sum = 0;

        for (CartProduct cp : products) {
            sum += cp.getProductCost();
        }
        return sum;
    }
}
