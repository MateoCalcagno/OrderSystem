package ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ordersystem.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}