package ordersystem;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponseDTO> getAll() {
        return orderRepository.findAll()
            .stream()
            .map(order -> new OrderResponseDTO(
                order.getId(),
                order.getProducts()
                    .stream()
                    .map(Product::getName)
                    .toList()
            ))
            .toList();
    }

    public OrderResponseDTO create(OrderRequestDTO dto) {
        List<Product> products = new ArrayList<>();

        for (Long id : dto.getProductIds()) {
            Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Producto no existe con id: " + id
                ));

            products.add(product);
        }

        Order order = new Order();
        order.setProducts(products);

        Order saved = orderRepository.save(order);

        // transformar a DTO
        List<String> productNames = saved.getProducts()
            .stream()
            .map(Product::getName)
            .toList();

        return new OrderResponseDTO(saved.getId(), productNames);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}