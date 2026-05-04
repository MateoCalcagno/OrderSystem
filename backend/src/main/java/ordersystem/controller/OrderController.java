package ordersystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import ordersystem.service.OrderService;
import ordersystem.dto.OrderResponseDTO;
import ordersystem.dto.OrderRequestDTO;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public Page<OrderResponseDTO> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAll(PageRequest.of(page, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDTO create(@RequestBody @Valid OrderRequestDTO dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}