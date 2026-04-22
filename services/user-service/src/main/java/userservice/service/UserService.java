package userservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import userservice.repository.UserRepository;
import userservice.security.JwtService;
import userservice.model.User;
import userservice.dto.*;
import userservice.exception.BadRequestException;
import userservice.mapper.UserMapper;

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

    public void register(RegisterDTO dto) {
        if (repository.findByUsername(dto.getUsername()).isPresent())
            throw new BadRequestException("El nombre de usuario ya está registrado");
        if (repository.findByEmail(dto.getEmail()).isPresent())
            throw new BadRequestException("El email ya está registrado");
        if (repository.findByDni(dto.getDni()).isPresent())
            throw new BadRequestException("El DNI ya está registrado");

        repository.save(UserMapper.toEntity(dto, passwordEncoder.encode(dto.getPassword())));
    }

    public AuthResponseDTO login(LoginDTO dto) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User user = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponseDTO(token, user.getUsername(), user.getRole().name());
    }

    public List<UserResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}
