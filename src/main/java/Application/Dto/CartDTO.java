package Application.Dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CartDTO {
    private final List<CartProductDTO> products;
    private final int productCount;
    private final double totalCost;

    public CartDTO(List<CartProductDTO> products, int productCount, double totalCost) {
        this.products = Collections.unmodifiableList(new ArrayList<>(products));
        this.productCount = productCount;
        this.totalCost = totalCost;
    }

    public List<CartProductDTO> getProducts() {
        return products;
    }

    public int getProductCount() {
        return productCount;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
