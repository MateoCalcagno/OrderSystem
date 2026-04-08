package ordersystem.mapper;

import ordersystem.model.Order;
import ordersystem.model.Product;
import ordersystem.dto.OrderResponseDTO;

public class OrderMapper {

    public static OrderResponseDTO toDTO(Order order) {
        return new OrderResponseDTO(
            order.getId(),
            order.getProducts().stream()
                .map(Product::getName)
                .toList(),
            order.getUser().getUsername(),
            order.getCreatedAt()
        );
    }
}