package ordersystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private List<String> products;
    private String username; 
    private LocalDateTime createdAt; 
    private BigDecimal totalPrice; 

    public OrderResponseDTO(Long id, List<String> products, String username, LocalDateTime createdAt, BigDecimal totalPrice) {
        this.id = id;
        this.products = products;
        this.username = username;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice; 
    }

    public Long getId() { return id; }
    public List<String> getProducts() { return products; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; } 
    public BigDecimal getTotalPrice() { return totalPrice; } 
}