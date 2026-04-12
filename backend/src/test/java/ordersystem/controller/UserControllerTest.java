package ordersystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.AuthResponseDTO;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.RegisterDTO;
import ordersystem.dto.UserResponseDTO;
import ordersystem.model.Role;
import ordersystem.security.JwtService;
import ordersystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_conDatosValidos_deberiaRetornar200() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("m@m.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");

        doNothing().when(userService).register(any());

        mockMvc.perform(post("/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void register_conDatosInvalidos_deberiaRetornar400() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("ma");
        dto.setPassword("123456");
        dto.setEmail("emailinvalido");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");

        mockMvc.perform(post("/users/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void login_conCredencialesValidas_deberiaRetornarToken() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");

        when(userService.login(any())).thenReturn(
            new AuthResponseDTO("fake-token", "mateo", "USER")
        );

        mockMvc.perform(post("/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-token"))
            .andExpect(jsonPath("$.username").value("mateo"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_comoAdmin_deberiaRetornar200() throws Exception {
        when(userService.getAll()).thenReturn(List.of(
            new UserResponseDTO("mateo", Role.USER),
            new UserResponseDTO("admin", Role.ADMIN)
        ));

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }
}