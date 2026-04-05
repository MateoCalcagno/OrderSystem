package ordersystem.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

import ordersystem.repository.*;
import ordersystem.model.*;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.dto.OrderRequestDTO;
import ordersystem.exception.ResourceNotFoundException;;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getAll() {
        // 1. Obtener el nombre del usuario logueado
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 2. Buscar al usuario para conocer su rol
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Order> orders;

        // 3. Lógica de visibilidad
        if (user.getRole() == Role.ADMIN) {
            orders = orderRepository.findAll(); // Admin ve todo
        } else {
            orders = user.getOrders(); // User ve solo sus pedidos (Funciona gracias al @Transactional)
        }

        // 4. Mapeo a DTO
        return orders.stream()
            .map(order -> new OrderResponseDTO(
                order.getId(),
                order.getProducts().stream().map(Product::getName).toList(),
                order.getUser().getUsername(),
                order.getCreatedAt() // <-- nueva línea
            ))
            .toList();
    }

    @Transactional
    public OrderResponseDTO create(OrderRequestDTO dto) {
        // 1. Obtener usuario actual
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 2. Buscar los productos
        List<Product> products = new ArrayList<>();
        for (Long id : dto.getProductIds()) {
            Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no existe con id: " + id));
            products.add(product);
        }

        // 3. Crear y guardar la orden
        Order order = new Order();
        order.setProducts(products);
        order.setUser(user); 

        Order saved = orderRepository.save(order);

        return new OrderResponseDTO(
            saved.getId(), 
            saved.getProducts().stream().map(Product::getName).toList(), 
            saved.getUser().getUsername(),
            saved.getCreatedAt()
        );
    }

    @Transactional
    public void delete(Long id) {
        // 1. Buscar la orden
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        // 2. Obtener usuario actual
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
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