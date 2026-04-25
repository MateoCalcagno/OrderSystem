package ordersystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.model.Product;
import ordersystem.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    // ── HELPERS ─────────────────────────────────────────

    private Product product(String name) {
        Product p = new Product(name);
        p.setPrice(new BigDecimal("10.00"));
        return p;
    }

    private Pageable pageable() {
        return PageRequest.of(0, 10);
    }

    private ProductRequestDTO dto(String name) {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName(name);
        return dto;
    }

    // ── GET ALL ─────────────────────────────────────────

    @Test
    void getAll_sinFiltro_deberiaUsarFindAll() {
        when(repository.findAll(pageable()))
            .thenReturn(new PageImpl<>(List.of(product("Pizza"), product("Sushi"))));

        Page<ProductResponseDTO> result = productService.getAll(pageable(), "");

        assertEquals(2, result.getContent().size());
        verify(repository).findAll(pageable());
        verify(repository, never()).findByNameContaining(any(), any());
    }

    @Test
    void getAll_conFiltro_deberiaUsarFindByNameContaining() {
        when(repository.findByNameContaining("pizza", pageable()))
            .thenReturn(new PageImpl<>(List.of(product("Pizza"))));

        Page<ProductResponseDTO> result = productService.getAll(pageable(), "pizza");

        assertEquals(1, result.getContent().size());
        assertEquals("Pizza", result.getContent().get(0).getName());

        verify(repository).findByNameContaining("pizza", pageable());
        verify(repository, never()).findAll(pageable());
    }

    // ── CREATE ─────────────────────────────────────────

    @Test
    void create_deberiaCapitalizarYGuardar() {
        Product saved = new Product("Pizza Margarita");
        saved.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(saved);

        ProductResponseDTO result = productService.create(dto("pizza margarita"));

        assertEquals("Pizza Margarita", result.getName());
    }

    // ── GET BY ID ───────────────────────────────────────

    @Test
    void getById_existente() {
        Product product = product("Pizza");
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        assertEquals("Pizza", productService.getById(1L).getName());
    }

    @Test
    void getById_noExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getById(99L));
    }

    // ── UPDATE ─────────────────────────────────────────

    @Test
    void update_existente() {
        Product product = product("Pizza");
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProductResponseDTO result = productService.update(1L, dto("pizza napolitana"));

        assertEquals("Pizza Napolitana", result.getName());
    }

    @Test
    void update_noExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.update(99L, dto("pizza")));
    }

    // ── DELETE ─────────────────────────────────────────

    @Test
    void delete_existente() {
        when(repository.existsById(1L)).thenReturn(true);

        productService.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_noExistente() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.delete(99L));
    }
}