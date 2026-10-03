package Application.Model;

public class CartProduct {
    private Product product;
    private int amountInCart;

    public CartProduct(int amountInCart, Product product) {
        this.amountInCart = amountInCart;
        this.product = product;
    }

    public double getProductCost() {
        return product.getCost() * amountInCart;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmountInCart() {
        return amountInCart;
    }
}
