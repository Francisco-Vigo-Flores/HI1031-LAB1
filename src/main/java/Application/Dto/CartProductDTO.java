package Application.Dto;

/** A cart or order line with its total cost calculated by the application. */
public final class CartProductDTO {
    private final int amountInCart;
    private final ProductDTO product;
    private final double productCost;

    public CartProductDTO(int amountInCart, ProductDTO product, double productCost) {
        this.amountInCart = amountInCart;
        this.product = product;
        this.productCost = productCost;
    }

    public int getAmountInCart() {
        return amountInCart;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public double getProductCost() {
        return productCost;
    }
}
