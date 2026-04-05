package ordersystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private List<String> products;
    private String username; 
    private LocalDateTime createdAt; 

    public OrderResponseDTO(Long id, List<String> products, String username, LocalDateTime createdAt) {
        this.id = id;
        this.products = products;
        this.username = username;
        this.createdAt = createdAt; 
    }

    public Long getId() { return id; }
    public List<String> getProducts() { return products; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; } 
}