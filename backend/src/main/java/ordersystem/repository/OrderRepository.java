package ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ordersystem.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}