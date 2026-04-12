package ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.OrderRequestDTO;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.security.JwtService;
import ordersystem.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules();

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_comoUser_deberiaRetornar200() throws Exception {
        when(orderService.getAll()).thenReturn(List.of(
            new OrderResponseDTO(1L, List.of("Pizza"), "mateo", LocalDateTime.now())
        ));

        mockMvc.perform(get("/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].username").value("mateo"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_conProductosValidos_deberiaRetornar200() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of(1L, 2L));

        when(orderService.create(any())).thenReturn(
            new OrderResponseDTO(1L, List.of("Pizza", "Sushi"), "mateo", LocalDateTime.now())
        );

        mockMvc.perform(post("/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.products.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "USER")
    void create_conListaVacia_deberiaRetornar400() throws Exception {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of());

        mockMvc.perform(post("/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void delete_comoUser_deberiaRetornar200() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/orders/1").with(csrf()))
            .andExpect(status().isOk());
    }
}