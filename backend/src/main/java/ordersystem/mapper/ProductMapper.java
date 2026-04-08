package ordersystem.mapper;

import ordersystem.model.Product;
import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;

public class ProductMapper {

    public static ProductResponseDTO toDTO(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getName()
        );
    }

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        return product;
    }
}