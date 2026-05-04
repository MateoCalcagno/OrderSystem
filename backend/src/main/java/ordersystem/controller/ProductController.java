package ordersystem.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
    public Page<ProductResponseDTO> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String search
    ) {
        return service.getAll(PageRequest.of(page, size), search);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}