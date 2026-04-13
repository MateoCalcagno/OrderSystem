package ordersystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.model.Product;
import ordersystem.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAll_deberiaRetornarListaDeProductos() {
        when(repository.findAll()).thenReturn(List.of(
            new Product("Pizza"),
            new Product("Sushi")
        ));

        List<ProductResponseDTO> result = productService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void create_deberiaCapitalizarNombreYGuardar() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("pizza margarita");

        Product saved = new Product("Pizza Margarita");
        saved.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(saved);

        ProductResponseDTO result = productService.create(dto);

        assertEquals("Pizza Margarita", result.getName());
    }

    @Test
    void getById_conIdExistente_eInexistente() {
        Product product = new Product("Pizza");
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        assertEquals("Pizza", productService.getById(1L).getName());

        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    void update_deberiaActualizarONoEncontrar() {
        Product product = new Product("Pizza");
        product.setId(1L);

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("pizza napolitana");

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        assertEquals("Pizza Napolitana", productService.update(1L, dto).getName());

        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.update(99L, dto));
    }

    @Test
    void delete_deberiaBorrarONoEncontrar() {
        when(repository.existsById(1L)).thenReturn(true);
        productService.delete(1L);
        verify(repository).deleteById(1L);

        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(99L));
    }
}