package ordersystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private UserRepository userRepository;
    @Mock private AuthService authService;

    @InjectMocks
    private OrderService orderService;

    // ── HELPERS ───────────────────────────────────────────

    private User user(String username, Role role) {
        return new User(username, "pass", role, "", "", "", "");
    }

    private OrderRequestDTO orderRequest(Long... productIds) {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of(productIds));
        return dto;
    }

    private Pageable pageable() {
        return PageRequest.of(0, 10);
    }

    private void mockUsername(String username) {
        when(authService.getCurrentUsername()).thenReturn(username);
    }

    private void mockUser(User user) {
        when(authService.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
    }

    private void mockSecurityContext(Role role) {
        Authentication auth = mock(Authentication.class);
        when(auth.getAuthorities()).thenAnswer(inv ->
            List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }

    private Order buildOrder(User user) {
        Order order = new Order(List.of(new Product("Pizza")));
        order.setUser(user);
        return order;
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ── CREATE ───────────────────────────────────────────

    @Test
    void create_ok() {
        User user = user("mateo", Role.USER);

        Product product = new Product("Pizza");
        product.setId(1L);
        product.setPrice(new BigDecimal("10.00"));

        mockUser(user);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderResponseDTO result = orderService.create(orderRequest(1L));

        assertEquals(1, result.getProducts().size());
    }

    @Test
    void create_productoNoExiste() {
        User user = user("mateo", Role.USER);

        mockUser(user);

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.create(orderRequest(99L)));
    }

    // ── GET ALL ─────────────────────────────────────────

    @Test
    void getAll_admin() {
        User admin = user("admin", Role.ADMIN);
        Order order = buildOrder(admin);

        mockUser(admin);

        when(orderRepository.findAllWithProducts(pageable()))
                .thenReturn(new PageImpl<>(List.of(order)));

        assertEquals(1, orderService.getAll(pageable()).getContent().size());
    }

    @Test
    void getAll_user() {
        User user = user("mateo", Role.USER);
        Order order = buildOrder(user);

        mockUser(user);

        when(orderRepository.findByUserUsernameWithProducts("mateo", pageable()))
                .thenReturn(new PageImpl<>(List.of(order)));

        assertEquals(1, orderService.getAll(pageable()).getContent().size());
    }

    // ── DELETE ─────────────────────────────────────────

    @Test
    void delete_owner() {
        when(orderRepository.findOwnerUsernameById(1L)).thenReturn(Optional.of("mateo"));
        mockUsername("mateo");
        mockSecurityContext(Role.USER);

        orderService.delete(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void delete_admin() {
        when(orderRepository.findOwnerUsernameById(1L)).thenReturn(Optional.of("mateo"));
        mockUsername("admin");
        mockSecurityContext(Role.ADMIN);

        orderService.delete(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void delete_forbidden() {
        when(orderRepository.findOwnerUsernameById(1L)).thenReturn(Optional.of("mateo"));
        mockUsername("otro");
        mockSecurityContext(Role.USER);

        assertThrows(AccessDeniedException.class,
                () -> orderService.delete(1L));
    }
}