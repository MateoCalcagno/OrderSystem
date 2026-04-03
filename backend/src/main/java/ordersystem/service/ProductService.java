package ordersystem.service;

import org.springframework.stereotype.Service;
import java.util.List;

import ordersystem.repository.ProductRepository;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.model.Product;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product create(Product product) {
        product.setName(capitalize(product.getName()));
        return repository.save(product);
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public Product update(Long id, Product updatedProduct) {
        return repository.findById(id)
                .map(product -> {
                    product.setName(capitalize(updatedProduct.getName()));
                    return repository.save(product);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        repository.deleteById(id);
    }

    public String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;

        String[] words = text.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
            }
        }

        return result.toString().trim();
    }
}