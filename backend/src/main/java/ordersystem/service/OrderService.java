package ordersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

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
    public List<OrderResponseDTO> getAll() {
        // 1. Obtener el nombre del usuario logueado
        String currentUsername = authService.getCurrentUsername();
        
        // 2. Buscar al usuario para conocer su rol
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Order> orders;

        // 3. Lógica de visibilidad
        if (user.getRole() == Role.ADMIN) {
            orders = orderRepository.findAllWithProducts();
        } else {
            orders = orderRepository.findByUserUsernameWithProducts(currentUsername);
        }

        // 4. Mapeo a DTO
        return orders.stream()
            .map(OrderMapper::toDTO)
            .toList();
    }

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO dto) {
        // 1. Obtener usuario actual
        String currentUsername = authService.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 2. Buscar los productos
        List<Long> requestedIds = dto.getProductIds();
        List<Product> products = productRepository.findAllById(requestedIds);

        List<Long> foundIds = products.stream().map(Product::getId).toList();
        List<Long> missingIds = requestedIds.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();

        if (!missingIds.isEmpty()) {
            throw new ResourceNotFoundException("Productos no encontrados: " + missingIds);
        }

        // 3. Crear y guardar la orden
        Order order = new Order();
        order.setProducts(products);
        order.setUser(user); 

        Order saved = orderRepository.save(order);

        return OrderMapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        // 1. Buscar la orden
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        // 2. Obtener usuario actual
        String currentUsername = authService.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));

        // 3. Validación de permisos
        boolean isOwner = order.getUser().getUsername().equals(currentUsername);
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (isOwner || isAdmin) {
            orderRepository.delete(order);
        } else {
            throw new AccessDeniedException("No tenés permiso para borrar esta orden");
        }
    }
}