package ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ordersystem.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.products WHERE o.user.username = :username")
    Page<Order> findByUserUsernameWithProducts(@Param("username") String username, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.products")
    Page<Order> findAllWithProducts(Pageable pageable);

    @Query("SELECT o.user.username FROM Order o WHERE o.id = :id")
    Optional<String> findOwnerUsernameById(@Param("id") Long id);   
}