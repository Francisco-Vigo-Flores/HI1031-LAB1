package Application.Dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Order details with DTOs for the customer and line items. */
public final class OrderDTO {
    private final int id;
    private final boolean complete;
    private final UserDTO user;
    private final List<CartProductDTO> products;

    public OrderDTO(int id, boolean complete, UserDTO user, List<CartProductDTO> products) {
        this.id = id;
        this.complete = complete;
        this.user = user;
        this.products = Collections.unmodifiableList(new ArrayList<>(products));
    }

    public int getId() {
        return id;
    }

    public boolean isComplete() {
        return complete;
    }

    public UserDTO getUser() {
        return user;
    }

    public List<CartProductDTO> getProducts() {
        return products;
    }
}
