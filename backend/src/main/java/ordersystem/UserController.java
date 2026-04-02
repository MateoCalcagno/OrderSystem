package ordersystem;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody @Valid LoginDTO dto) {
        return service.register(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.getAll();
    }
}