package ordersystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Test
    void create_deberiaCrearOrdenCorrectamente() {
        // ARRANGE
        User user = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");
        Product product = new Product("Pizza");
        product.setId(1L);

        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of(1L));

        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        // ACT
        OrderResponseDTO result = orderService.create(dto);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getProducts().size());
        assertEquals("Pizza", result.getProducts().get(0));
        assertEquals("mateo", result.getUsername());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void create_conProductoInexistente_deberiaLanzarExcepcion() {
        // ARRANGE
        User user = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");

        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of(99L));

        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> orderService.create(dto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAll_comoAdmin_deberiaVerTodasLasOrdenes() {
        // ARRANGE
        User admin = new User("admin", "pass", Role.ADMIN, "a@a.com", "456", "Admin", "Admin");

        Order order1 = new Order(List.of(new Product("Pizza")));
        order1.setUser(admin);
        Order order2 = new Order(List.of(new Product("Sushi")));
        order2.setUser(admin);

        when(authService.getCurrentUsername()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(orderRepository.findAllWithProducts()).thenReturn(List.of(order1, order2));

        // ACT
        List<OrderResponseDTO> result = orderService.getAll();

        // ASSERT
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAllWithProducts();
        verify(orderRepository, never()).findByUserUsernameWithProducts(any());
    }

    @Test
    void getAll_comoUser_deberiaSoloVerSusOrdenes() {
        // ARRANGE
        User user = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");

        Order order = new Order(List.of(new Product("Pizza")));
        order.setUser(user);

        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(orderRepository.findByUserUsernameWithProducts("mateo")).thenReturn(List.of(order));

        // ACT
        List<OrderResponseDTO> result = orderService.getAll();

        // ASSERT
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByUserUsernameWithProducts("mateo");
        verify(orderRepository, never()).findAllWithProducts();
    }

    @Test
    void delete_comoOwner_deberiaBorrarOrden() {
        // ARRANGE
        User user = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");
        Order order = new Order(List.of());
        order.setUser(user);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(authService.getCurrentUsername()).thenReturn("mateo");
        when(userRepository.findByUsername("mateo")).thenReturn(Optional.of(user));

        // ACT
        orderService.delete(1L);

        // ASSERT
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void delete_comoOtroUser_deberiaLanzarAccessDeniedException() {
        // ARRANGE
        User owner = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");
        User otroUser = new User("pedro", "pass", Role.USER, "p@p.com", "789", "Pedro", "Garcia");
        Order order = new Order(List.of());
        order.setUser(owner);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(authService.getCurrentUsername()).thenReturn("pedro");
        when(userRepository.findByUsername("pedro")).thenReturn(Optional.of(otroUser));

        // ACT & ASSERT
        assertThrows(AccessDeniedException.class, () -> orderService.delete(1L));
        verify(orderRepository, never()).delete(any());
    }
}