package productservice.dto;

import java.math.BigDecimal;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponseDTO(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
}
