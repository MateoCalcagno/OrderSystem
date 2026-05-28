package ordersystem.mapper;

import ordersystem.model.Order;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.dto.OrderItemResponseDTO;

import java.util.List;

public class OrderMapper {

    public static OrderResponseDTO toDTO(Order order) {
        List<OrderItemResponseDTO> items = order.getItems().stream()
            .map(item -> new OrderItemResponseDTO(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice()
            ))
            .toList();

        return new OrderResponseDTO(
            order.getId(),
            items,
            order.getUser().getUsername(),
            order.getCreatedAt(),
            order.getTotalPrice()  
        );
    }
}