package ordersystem.service;

import org.springframework.stereotype.Service;
import java.util.List;

import ordersystem.repository.ProductRepository;
import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.mapper.ProductMapper;
import ordersystem.model.Product;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponseDTO> getAll() {
        return repository.findAll().stream()
            .map(ProductMapper::toDTO)
            .toList();
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        product.setName(capitalize(product.getName()));

        return ProductMapper.toDTO(repository.save(product));
    }

    public ProductResponseDTO getById(Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        return ProductMapper.toDTO(product);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        product.setName(capitalize(dto.getName()));

        return ProductMapper.toDTO(repository.save(product));
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