package ordersystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import ordersystem.dto.AuthResponseDTO;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.RegisterDTO;
import ordersystem.dto.UserResponseDTO;
import ordersystem.exception.BadRequestException;
import ordersystem.model.Role;
import ordersystem.model.User;
import ordersystem.repository.UserRepository;
import ordersystem.security.JwtService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void register_deberiaGuardarUsuarioCorrectamente() {
        // ARRANGE
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("m@m.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");

        when(repository.findByUsername("mateo")).thenReturn(Optional.empty());
        when(repository.findByEmail("m@m.com")).thenReturn(Optional.empty());
        when(repository.findByDni("12345678")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashedpass");

        // ACT
        userService.register(dto);

        // ASSERT
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void register_conUsernameExistente_deberiaLanzarExcepcion() {
        // ARRANGE
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("m@m.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");

        when(repository.findByUsername("mateo"))
            .thenReturn(Optional.of(new User()));

        // ACT & ASSERT
        assertThrows(BadRequestException.class, () -> userService.register(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void register_conEmailExistente_deberiaLanzarExcepcion() {
        // ARRANGE
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("m@m.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");

        when(repository.findByUsername("mateo")).thenReturn(Optional.empty());
        when(repository.findByEmail("m@m.com"))
            .thenReturn(Optional.of(new User()));

        // ACT & ASSERT
        assertThrows(BadRequestException.class, () -> userService.register(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void register_conDniExistente_deberiaLanzarExcepcion() {
        // ARRANGE
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("m@m.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Lopez");

        when(repository.findByUsername("mateo")).thenReturn(Optional.empty());
        when(repository.findByEmail("m@m.com")).thenReturn(Optional.empty());
        when(repository.findByDni("12345678"))
            .thenReturn(Optional.of(new User()));

        // ACT & ASSERT
        assertThrows(BadRequestException.class, () -> userService.register(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void login_conCredencialesValidas_deberiaRetornarToken() {
        // ARRANGE
        LoginDTO dto = new LoginDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");

        User user = new User("mateo", "hashedpass", Role.USER, "m@m.com", "123", "Mateo", "Lopez");

        when(repository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("mateo", "USER")).thenReturn("fake-jwt-token");

        // ACT
        AuthResponseDTO result = userService.login(dto);

        // ASSERT
        assertNotNull(result);
        assertEquals("fake-jwt-token", result.getToken());
        assertEquals("mateo", result.getUsername());
        assertEquals("USER", result.getRole());
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void getAll_deberiaRetornarListaDeUsuarios() {
        // ARRANGE
        when(repository.findAll()).thenReturn(List.of(
            new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Lopez"),
            new User("admin", "pass", Role.ADMIN, "a@a.com", "456", "Admin", "Admin")
        ));

        // ACT
        List<UserResponseDTO> result = userService.getAll();

        // ASSERT
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }
}