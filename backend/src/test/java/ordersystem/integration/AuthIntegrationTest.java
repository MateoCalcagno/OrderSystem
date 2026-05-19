package ordersystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RegisterDTO buildRegisterDTO() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("mateo@test.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");
        return dto;
    }

    @Test
    void register_y_login_flujoCompleto() throws Exception {

        // 1. Register
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildRegisterDTO())))
            .andExpect(status().isCreated());

        // 2. Login
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("mateo");
        loginDTO.setPassword("123456");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.username").value("mateo"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_conCredencialesInvalidas_deberiaRetornar401() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("noexiste");
        loginDTO.setPassword("wrongpass");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
            .andExpect(status().isUnauthorized());
    }
}