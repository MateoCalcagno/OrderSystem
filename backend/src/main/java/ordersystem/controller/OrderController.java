package ordersystem.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import ordersystem.service.OrderService;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.dto.OrderRequestDTO;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderResponseDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public OrderResponseDTO create(@RequestBody @Valid OrderRequestDTO dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}