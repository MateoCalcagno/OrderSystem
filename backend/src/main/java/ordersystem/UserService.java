package ordersystem;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(LoginDTO dto) {
        // 1. Validar si el usuario ya existe
        if (repository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está tomado");
        }

        // 2. Crear la ENTIDAD a partir del DTO
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.USER); // Por defecto USER si viene null

        // 3. Encriptar la contraseña que viene del DTO
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encodedPassword);
        
        // 4. Guardar la ENTIDAD
        User saved = repository.save(user);

        // 5. Retornar el DTO de respuesta (para no mostrar la password)
        return new UserResponseDTO(saved.getUsername(), saved.getRole());
    }

    public List<UserResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getUsername(), user.getRole()))
                .toList();
    }
}