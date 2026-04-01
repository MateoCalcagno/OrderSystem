package ordersystem;

import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private List<String> products;

    public OrderResponseDTO(Long id, List<String> products) {
        this.id = id;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public List<String> getProducts() {
        return products;
    }
}