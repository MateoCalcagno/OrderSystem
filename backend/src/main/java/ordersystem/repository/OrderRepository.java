package ordersystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ordersystem.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.products WHERE o.user.username = :username")
    List<Order> findByUserUsernameWithProducts(@Param("username") String username);

    @Query("SELECT o FROM Order o JOIN FETCH o.products")
    List<Order> findAllWithProducts();
}