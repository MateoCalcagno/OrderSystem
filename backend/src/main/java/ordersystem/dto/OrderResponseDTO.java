package ordersystem.dto;

import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private List<String> products;
    private String username; 

    public OrderResponseDTO(Long id, List<String> products, String username) {
        this.id = id;
        this.products = products;
        this.username = username;
    }

    public Long getId() { return id; }
    public List<String> getProducts() { return products; }
    public String getUsername() { return username; }
}