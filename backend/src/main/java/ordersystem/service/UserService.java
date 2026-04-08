package ordersystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.HashMap;
import java.util.Map;

import ordersystem.repository.UserRepository;
import ordersystem.security.JwtService;
import ordersystem.model.User;
import ordersystem.dto.UserResponseDTO;
import ordersystem.mapper.UserMapper;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.RegisterDTO;
import ordersystem.model.Role;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public UserService(UserRepository repository,
                    PasswordEncoder passwordEncoder,
                    AuthenticationManager authManager,
                    JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public UserResponseDTO register(RegisterDTO dto) {
        // 1. Validar si el usuario ya existe
        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está tomado");
        }

        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (repository.findByDni(dto.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        // 2. Crear la ENTIDAD a partir del DTO
        User user = new User(
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()), // encriptamos directamente
            Role.USER,  // rol por defecto
            dto.getEmail(),
            dto.getDni(),
            dto.getFirstName(),
            dto.getLastName()
        );

        // 3. Guardar la ENTIDAD
        User saved = repository.save(user);

        // 4. Retornar el DTO de respuesta (para no mostrar la password)
        return UserMapper.toDTO(saved);
    }

    public Map<String, String> login(LoginDTO dto) {
        // 1. Autenticar usuario
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword()
            )
        );

        // 2. Generar token
        User user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        // 3. Devolver token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

    public List<UserResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}