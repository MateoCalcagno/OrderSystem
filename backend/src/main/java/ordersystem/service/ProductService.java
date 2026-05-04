package ordersystem.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ordersystem.repository.ProductRepository;
import ordersystem.dto.ProductRequestDTO;
import ordersystem.dto.ProductResponseDTO;
import ordersystem.exception.ResourceNotFoundException;
import ordersystem.mapper.ProductMapper;
import ordersystem.model.Product;
import ordersystem.util.StringUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAll(Pageable pageable, String search) {
        if (search != null && !search.isBlank()) {
            return repository.findByNameContaining(search, pageable).map(ProductMapper::toDTO);
        }
        return repository.findAll(pageable).map(ProductMapper::toDTO);
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = ProductMapper.toEntity(dto);
        product.setName(StringUtils.capitalize(product.getName()));

        return ProductMapper.toDTO(repository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getById(Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        return ProductMapper.toDTO(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        product.setName(StringUtils.capitalize(dto.getName()));
        product.setPrice(dto.getPrice()); 

        return ProductMapper.toDTO(repository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        repository.deleteById(id);
    }
}