package ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.OrderRequestDTO;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.security.JwtService;
import ordersystem.service.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtService jwtService;

    // ── CREATE ─────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    void create_requestValido_deberiaRetornarOk() throws Exception {

        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setProductIds(List.of(1L, 2L));

        when(orderService.create(any())).thenReturn(
            new OrderResponseDTO(
                1L,
                List.of("Pizza", "Sushi"),
                "mateo",
                LocalDateTime.now(),
                new BigDecimal("25.00")
            )
        );

        mockMvc.perform(post("/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products.length()").value(2))
                .andExpect(jsonPath("$.totalPrice").value(25.00));

        verify(orderService).create(any());
    }

    // ── GET ALL ─────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    void getAll_usuarioAutenticado_deberiaRetornarOk() throws Exception {

        Page<OrderResponseDTO> page = new PageImpl<>(List.of(
            new OrderResponseDTO(
                1L,
                List.of("Pizza"),
                "mateo",
                LocalDateTime.now(),
                new BigDecimal("10.00")
            )
        ));

        when(orderService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].username").value("mateo"))
                .andExpect(jsonPath("$.content[0].totalPrice").value(10.00));

        verify(orderService).getAll(any());
    }

    // ── DELETE ─────────────────────────────────────────

    @Test
    @WithMockUser(roles = "USER")
    void delete_conIdValido_deberiaRetornar204() throws Exception {

        mockMvc.perform(delete("/orders/1")
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(orderService).delete(1L);
    }
}