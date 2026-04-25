package ordersystem.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import ordersystem.dto.*;
import ordersystem.exception.BadRequestException;
import ordersystem.model.Role;
import ordersystem.model.User;
import ordersystem.repository.UserRepository;
import ordersystem.security.JwtService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authManager;
    @Mock private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    // ── HELPERS ─────────────────────────────────────────

    private RegisterDTO dto() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");
        dto.setEmail("m@m.com");
        dto.setDni("12345678");
        dto.setFirstName("Mateo");
        dto.setLastName("Calcagno");
        return dto;
    }

    private void noDuplicates() {
        when(repository.findByUsername("mateo")).thenReturn(Optional.empty());
        when(repository.findByEmail("m@m.com")).thenReturn(Optional.empty());
        when(repository.findByDni("12345678")).thenReturn(Optional.empty());
    }

    // ── REGISTER ───────────────────────────────────────

    @Test
    void register_ok_deberiaGuardarYEncriptarPassword() {
        noDuplicates();
        when(passwordEncoder.encode("123456")).thenReturn("hashed");

        userService.register(dto());

        verify(passwordEncoder).encode("123456");
        verify(repository).save(any(User.class));
    }

    @Test
    void register_usernameDuplicado_deberiaFallar() {
        when(repository.findByUsername("mateo"))
                .thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> userService.register(dto()));
        verify(repository, never()).save(any());
    }

    @Test
    void register_emailDuplicado_deberiaFallar() {
        when(repository.findByUsername("mateo")).thenReturn(Optional.empty());
        when(repository.findByEmail("m@m.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> userService.register(dto()));
        verify(repository, never()).save(any());
    }

    @Test
    void register_dniDuplicado_deberiaFallar() {
        when(repository.findByUsername("mateo")).thenReturn(Optional.empty());
        when(repository.findByEmail("m@m.com")).thenReturn(Optional.empty());
        when(repository.findByDni("12345678"))
                .thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> userService.register(dto()));
        verify(repository, never()).save(any());
    }

    // ── LOGIN ─────────────────────────────────────────

    @Test
    void login_ok_deberiaAutenticarYGenerarToken() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("mateo");
        dto.setPassword("123456");

        User user = new User("mateo", "pass", Role.USER, "m@m.com", "123", "Mateo", "Calcagno");

        when(repository.findByUsername("mateo")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("mateo", "USER")).thenReturn("token");

        AuthResponseDTO result = userService.login(dto);

        assertEquals("token", result.getToken());

        verify(authManager).authenticate(any());
        verify(jwtService).generateToken("mateo", "USER");
    }

    // ── GET ALL ───────────────────────────────────────

    @Test
    void getAll_deberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(new User(), new User()));

        assertEquals(2, userService.getAll().size());
    }
}