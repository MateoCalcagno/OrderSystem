package ordersystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.OrderRequestDTO;
import ordersystem.dto.ProductRequestDTO;
import ordersystem.model.Role;
import ordersystem.model.User;
import ordersystem.repository.OrderRepository;
import ordersystem.repository.ProductRepository;
import ordersystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.save(new User(
            "admin", passwordEncoder.encode("admin123"), Role.ADMIN,
            "admin@test.com", "99999999", "Admin", "Test"
        ));

        userRepository.save(new User(
            "user1", passwordEncoder.encode("user123"), Role.USER,
            "user1@test.com", "11111111", "User", "Test"
        ));
    }

    private String getToken(String username, String password) throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        MvcResult result = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
            .get("token").asText();
    }

    @Test
    void adminCreaProducto_userHaceOrden_yLaVe() throws Exception {

        String adminToken = getToken("admin", "admin123");
        String userToken = getToken("user1", "user123");

        // 1. Admin crea producto
        ProductRequestDTO productDTO = new ProductRequestDTO();
        productDTO.setName("Pizza");
        productDTO.setPrice(new BigDecimal("10.00"));

        MvcResult productResult = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Pizza"))
            .andReturn();

        Long productId = objectMapper.readTree(productResult.getResponse().getContentAsString())
            .get("id").asLong();

        // 2. User crea orden con ese producto
        OrderRequestDTO orderDTO = new OrderRequestDTO();
        orderDTO.setProductIds(List.of(productId));

        mockMvc.perform(post("/orders")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.products[0]").value("Pizza"))
            .andExpect(jsonPath("$.username").value("user1"));

        // 3. User ve su orden
        mockMvc.perform(get("/orders")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].username").value("user1"));

        // 4. Admin ve todas las ordenes
        mockMvc.perform(get("/orders")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void userNoPuedaCrearProducto() throws Exception {
        String userToken = getToken("user1", "user123");

        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Sushi");

        mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isForbidden());
    }
}