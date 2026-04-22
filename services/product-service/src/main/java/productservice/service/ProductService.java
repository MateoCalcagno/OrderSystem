package productservice.service;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import productservice.repository.ProductRepository;
import productservice.dto.ProductRequestDTO;
import productservice.dto.ProductResponseDTO;
import productservice.exception.ResourceNotFoundException;
import productservice.mapper.ProductMapper;
import productservice.model.Product;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Page<ProductResponseDTO> getAll(Pageable pageable, String search) {
        if (search != null && !search.isBlank())
            return repository.findByNameContaining(search, pageable).map(ProductMapper::toDTO);
        return repository.findAll(pageable).map(ProductMapper::toDTO);
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        product.setName(capitalize(product.getName()));
        return ProductMapper.toDTO(repository.save(product));
    }

    public ProductResponseDTO getById(Long id) {
        return ProductMapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setName(capitalize(dto.getName()));
        product.setPrice(dto.getPrice());
        return ProductMapper.toDTO(repository.save(product));
    }

    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Product not found");
        repository.deleteById(id);
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        String[] words = text.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty())
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return result.toString().trim();
    }
}
