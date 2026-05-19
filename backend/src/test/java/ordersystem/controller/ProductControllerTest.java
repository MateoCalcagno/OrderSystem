package ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.security.JwtService;
import ordersystem.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    // ── CREATE ─────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_admin_deberiaRetornarOk() throws Exception {

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Pizza");
        dto.setPrice(new BigDecimal("10.00"));

        when(productService.create(any()))
                .thenReturn(new ProductResponseDTO(1L, "Pizza", new BigDecimal("10.00")));

        mockMvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pizza"));

        verify(productService).create(any());
    }

    // ── GET ALL ─────────────────────────────

    @Test
    @WithMockUser
    void getAll_usuarioAutenticado_deberiaRetornarOk() throws Exception {

        Page<ProductResponseDTO> page = new PageImpl<>(List.of(
            new ProductResponseDTO(1L, "Pizza", new BigDecimal("10.00")),
            new ProductResponseDTO(2L, "Sushi", new BigDecimal("15.00"))
        ));

        when(productService.getAll(any(), any())).thenReturn(page);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Pizza"));

        verify(productService).getAll(any(), any());
    }

    // ── GET BY ID ───────────────────────

    @Test
    @WithMockUser
    void getById_existente_deberiaRetornarOk() throws Exception {

        when(productService.getById(1L))
                .thenReturn(new ProductResponseDTO(1L, "Pizza", new BigDecimal("10.00")));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza"));

        verify(productService).getById(1L);
    }

    @Test
    @WithMockUser
    void getById_inexistente_deberiaRetornar404() throws Exception {

        when(productService.getById(99L))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));
    }

    // ── UPDATE ─────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_admin_deberiaRetornarOk() throws Exception {

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Pizza Napolitana");
        dto.setPrice(new BigDecimal("12.00"));

        when(productService.update(eq(1L), any()))
                .thenReturn(new ProductResponseDTO(1L, "Pizza Napolitana", new BigDecimal("12.00")));

        mockMvc.perform(put("/products/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pizza Napolitana"));

        verify(productService).update(eq(1L), any());
    }

    // ── DELETE ─────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_admin_deberiaRetornar204() throws Exception {

        mockMvc.perform(delete("/products/1")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(productService).delete(1L);
    }
}