package ordersystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import ordersystem.dto.OrderRequestDTO;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.model.Order;
import ordersystem.model.Product;
import ordersystem.model.Role;
import ordersystem.model.User;
import ordersystem.repository.OrderRepository;
import ordersystem.repository.ProductRepository;
import ordersystem.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private OrderService orderService;

    private Order buildOrder(User user) {
        Order order = new Order(List.of(new Product("Pizza")));
        order.setUser(user);
        return order;
    }

    // ── CREATE ─────────────────────────────────────────────

    @Test
    void create_ok_yErrores() {
        User user = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");
        Product product = new Product("Pizza");
        product.setId(1L);

        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of(1L));

        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // ✔ caso correcto
        OrderResponseDTO result = orderService.create(dto);
        assertEquals(1, result.getProducts().size());

        // ❌ producto inexistente
        dto.setProductIds(List.of(99L));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.create(dto));
    }

    // ── GET ALL ───────────────────────────────────────────

    @Test
    void getAll_admin_y_user() {
        User admin = new User("admin", "pass", Role.ADMIN, "", "", "", "");
        User user = new User("mateo", "pass", Role.USER, "", "", "", "");

        Pageable pageable = PageRequest.of(0, 10);

        // ✔ admin
        Order orderAdmin = buildOrder(admin);

        when(authService.getCurrentUsername()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(orderRepository.findAllWithProducts(pageable))
            .thenReturn(new PageImpl<>(List.of(orderAdmin)));

        assertEquals(1, orderService.getAll(pageable).getContent().size());

        // ✔ user
        Order orderUser = buildOrder(user);

        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(orderRepository.findByUserUsernameWithProducts("mateo", pageable))
            .thenReturn(new PageImpl<>(List.of(orderUser)));

        assertEquals(1, orderService.getAll(pageable).getContent().size());
    }

    // ── DELETE ───────────────────────────────────────────

    @Test
    void delete_casosPrincipales() {
        User owner = new User("mateo", "", Role.USER, "", "", "", "");
        User admin = new User("admin", "", Role.ADMIN, "", "", "", "");

        Order order = buildOrder(owner);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // ✔ owner puede borrar
        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(owner));

        orderService.delete(1L);
        verify(orderRepository).delete(order);

        // ✔ admin puede borrar
        when(authService.getCurrentUsername()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        orderService.delete(1L);
        verify(orderRepository, times(2)).delete(order);

        // ❌ otro user no puede
        when(authService.getCurrentUsername()).thenReturn("otro");
        when(userRepository.findByUsername("otro"))
                .thenReturn(Optional.of(new User()));

        assertThrows(AccessDeniedException.class, () -> orderService.delete(1L));
    }
}