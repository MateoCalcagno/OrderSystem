package ordersystem.controller;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

import ordersystem.service.ProductService;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.dto.ProductRequestDTO;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ProductResponseDTO create(@RequestBody @Valid ProductRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}