package ordersystem;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequestDTO {

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    private List<Long> productIds;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}