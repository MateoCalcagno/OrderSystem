package ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.security.JwtService;
import ordersystem.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_comoUser_deberiaRetornar200() throws Exception {
        when(productService.getAll()).thenReturn(List.of(
            new ProductResponseDTO(1L, "Pizza"),
            new ProductResponseDTO(2L, "Sushi")
        ));

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_comoAdmin_deberiaRetornar200() throws Exception {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Pizza");

        when(productService.create(any())).thenReturn(new ProductResponseDTO(1L, "Pizza"));

        mockMvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_conIdExistente_deberiaRetornar200() throws Exception {
        when(productService.getById(1L)).thenReturn(new ProductResponseDTO(1L, "Pizza"));

        mockMvc.perform(get("/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_conIdInexistente_deberiaRetornar404() throws Exception {
        when(productService.getById(99L))
            .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

        mockMvc.perform(get("/products/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Producto no encontrado"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void update_comoAdmin_deberiaRetornar200() throws Exception {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Pizza Napolitana");

        when(productService.update(eq(1L), any()))
            .thenReturn(new ProductResponseDTO(1L, "Pizza Napolitana"));

        mockMvc.perform(put("/products/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Pizza Napolitana"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_comoAdmin_deberiaRetornar200() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/products/1").with(csrf()))
            .andExpect(status().isOk());
    }
}