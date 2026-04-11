package ordersystem.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import ordersystem.service.UserService;
import ordersystem.dto.UserResponseDTO;
import ordersystem.dto.AuthResponseDTO;
import ordersystem.dto.LoginDTO;
import ordersystem.dto.RegisterDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterDTO dto) {
        service.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO dto) {
        return service.login(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.getAll();
    }
}