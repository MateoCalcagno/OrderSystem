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

import ordersystem.dto.*;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.model.*;
import ordersystem.repository.*;

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

    // ───────────────────────── HELPERS ─────────────────────────

    private User user(String username, Role role) {
        return new User(username, "pass", role, "", "", "", "");
    }

    private OrderRequestDTO orderRequest(Long productId, int quantity) {
        OrderItemRequestDTO item = new OrderItemRequestDTO();
        item.setProductId(productId);
        item.setQuantity(quantity);

        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setItems(List.of(item));

        return dto;
    }

    private Pageable pageable() {
        return PageRequest.of(0, 10);
    }

    private void mockUser(User user) {
        when(authService.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
    }

    private void mockUsername(String username) {
        when(authService.getCurrentUsername()).thenReturn(username);
    }

    private void mockSecurity(Role role) {
        Authentication auth = mock(Authentication.class);

        when(auth.getAuthorities()).thenAnswer(inv ->
            List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );

        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(ctx);
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    // ───────────────────────── CREATE ─────────────────────────

    @Test
    void create_ok() {

        User user = user("mateo", Role.USER);
        mockUser(user);

        Product product = new Product("Pizza");
        product.setId(1L);
        product.setPrice(new BigDecimal("10.00"));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderResponseDTO result =
                orderService.create(orderRequest(1L, 2));

        assertEquals(1, result.getItems().size());
        assertEquals(new BigDecimal("20.00"), result.getTotalPrice());
    }

    @Test
    void create_productoNoExiste() {

        User user = user("mateo", Role.USER);
        mockUser(user);

        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.create(orderRequest(99L, 1)));
    }

    // ───────────────────────── GET ALL ─────────────────────────

    @Test
    void getAll_admin() {

        User admin = user("admin", Role.ADMIN);
        mockUser(admin);

        Order order = new Order();
        order.setUser(admin);

        when(orderRepository.findAllWithItems(pageable()))
                .thenReturn(new PageImpl<>(List.of(order)));

        var result = orderService.getAll(pageable());

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getAll_user() {

        User user = user("mateo", Role.USER);
        mockUser(user);

        Order order = new Order();
        order.setUser(user);

        when(orderRepository.findByUserUsernameWithItems("mateo", pageable()))
                .thenReturn(new PageImpl<>(List.of(order)));

        var result = orderService.getAll(pageable());

        assertEquals(1, result.getContent().size());
    }

    // ───────────────────────── DELETE ─────────────────────────

    @Test
    void delete_owner() {

        when(orderRepository.findOwnerUsernameById(1L))
                .thenReturn(Optional.of("mateo"));

        mockUsername("mateo");
        mockSecurity(Role.USER);

        orderService.delete(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void delete_admin() {

        when(orderRepository.findOwnerUsernameById(1L))
                .thenReturn(Optional.of("mateo"));

        mockUsername("admin");
        mockSecurity(Role.ADMIN);

        orderService.delete(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void delete_forbidden() {

        when(orderRepository.findOwnerUsernameById(1L))
                .thenReturn(Optional.of("mateo"));

        mockUsername("otro");
        mockSecurity(Role.USER);

        assertThrows(AccessDeniedException.class,
                () -> orderService.delete(1L));
    }
}