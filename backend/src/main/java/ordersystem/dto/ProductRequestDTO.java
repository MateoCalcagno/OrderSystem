package ordersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductRequestDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}