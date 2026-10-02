package Application;

public class CartProduct {
    Product product;
    int amountInCart;

    public CartProduct(int ammountInCart, Product product) {
        this.amountInCart = ammountInCart;
        this.product = product;
    }

}
