package ordersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ordersystem.repository.*;
import ordersystem.model.*;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.dto.OrderRequestDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.mapper.OrderMapper;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository,
                        AuthService authService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAll(Pageable pageable) {
        String currentUsername = authService.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (user.getRole() == Role.ADMIN) {
            return orderRepository.findAllWithItems(pageable)
                    .map(OrderMapper::toDTO);
        } else {
            return orderRepository.findByUserUsernameWithItems(currentUsername, pageable)
                    .map(OrderMapper::toDTO);
        }
    }

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO dto) {

        String username = authService.getCurrentUsername();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Order order = new Order();
        order.setUser(user);

        List<OrderItem> items = dto.getItems().stream()
            .map(itemDTO -> {

                Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado: " + itemDTO.getProductId()));

                return new OrderItem(
                    order,
                    product,
                    itemDTO.getQuantity(),
                    product.getPrice()
                );
            })
            .toList();

        order.setItems(items);

        Order saved = orderRepository.save(order);

        return OrderMapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        String ownerUsername = orderRepository.findOwnerUsernameById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        String currentUsername = authService.getCurrentUsername();

        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!ownerUsername.equals(currentUsername) && !isAdmin)
            throw new AccessDeniedException("No tenés permiso para borrar esta orden");

        orderRepository.deleteById(id);
    }
}