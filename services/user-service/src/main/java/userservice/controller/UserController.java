package userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import userservice.service.UserService;
import userservice.dto.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register(@RequestBody @Valid RegisterDTO dto) {
        service.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginDTO dto) {
        return service.login(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAll() {
        return service.getAll();
    }
}
