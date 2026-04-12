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
        // ARRANGE
        when(repository.findAll()).thenReturn(List.of(
            new Product("Pizza"),
            new Product("Sushi")
        ));

        // ACT
        List<ProductResponseDTO> result = productService.getAll();

        // ASSERT
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void create_deberiaCapitalizarNombreYGuardar() {
        // ARRANGE
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("pizza margarita");

        Product saved = new Product("Pizza Margarita");
        saved.setId(1L);

        when(repository.save(any(Product.class))).thenReturn(saved);

        // ACT
        ProductResponseDTO result = productService.create(dto);

        // ASSERT
        assertEquals("Pizza Margarita", result.getName());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void getById_conIdExistente_deberiaRetornarProducto() {
        // ARRANGE
        Product product = new Product("Pizza");
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        // ACT
        ProductResponseDTO result = productService.getById(1L);

        // ASSERT
        assertEquals("Pizza", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_conIdInexistente_deberiaLanzarExcepcion() {
        // ARRANGE
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    void update_deberiaActualizarNombreCorrectamente() {
        // ARRANGE
        Product product = new Product("Pizza");
        product.setId(1L);

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("pizza napolitana");

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // ACT
        ProductResponseDTO result = productService.update(1L, dto);

        // ASSERT
        assertEquals("Pizza Napolitana", result.getName());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void delete_conIdExistente_deberiaBorrar() {
        // ARRANGE
        when(repository.existsById(1L)).thenReturn(true);

        // ACT
        productService.delete(1L);

        // ASSERT
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_conIdInexistente_deberiaLanzarExcepcion() {
        // ARRANGE
        when(repository.existsById(99L)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(99L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void capitalize_deberiaCapitalizarCadaPalabra() {
        assertEquals("Pizza Margarita", productService.capitalize("pizza margarita"));
        assertEquals("Coca Cola", productService.capitalize("coca cola"));
        assertEquals("Agua", productService.capitalize("agua"));
    }
}