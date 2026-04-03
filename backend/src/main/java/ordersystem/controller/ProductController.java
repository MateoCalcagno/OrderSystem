package ordersystem.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import ordersystem.service.ProductService;
import ordersystem.model.Product;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.create(product);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}