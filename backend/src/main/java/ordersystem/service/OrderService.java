package ordersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ordersystem.repository.*;
import ordersystem.util.PriceCalculator;
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
            return orderRepository.findAllWithProducts(pageable)
                    .map(OrderMapper::toDTO);
        } else {
            return orderRepository.findByUserUsernameWithProducts(currentUsername, pageable)
                    .map(OrderMapper::toDTO);
        }
    }

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO dto) {
        String currentUsername = authService.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Product> products = dto.getProductIds().stream()
            .map(id -> productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado: " + id)))
            .toList();

        BigDecimal total = PriceCalculator.calculateTotal(products);

        Order order = new Order();
        order.setProducts(products);
        order.setUser(user);
        order.setTotalPrice(total); 

        return OrderMapper.toDTO(orderRepository.save(order));
    }

    @Transactional
    public void delete(Long id) {
        // 1. Buscar la orden
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        // 2. Obtener usuario actual
        String currentUsername = authService.getCurrentUsername();

        // 3. Validación de permisos
        boolean isOwner = order.getUser().getUsername().equals(currentUsername);
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));


        if (isOwner || isAdmin) {
            orderRepository.delete(order);
        } else {
            throw new AccessDeniedException("No tenés permiso para borrar esta orden");
        }
    }
}