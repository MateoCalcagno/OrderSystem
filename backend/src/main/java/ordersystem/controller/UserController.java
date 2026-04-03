package ordersystem.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import ordersystem.service.UserService;
import ordersystem.dto.UserResponseDTO;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.RegisterDTO;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody @Valid RegisterDTO dto) {
        return service.register(dto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO dto) {
        return service.login(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.getAll();
    }
}