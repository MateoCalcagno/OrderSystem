package ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ordersystem.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // JOIN FETCH sobre items (y su product) para evitar N+1
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items i JOIN FETCH i.product WHERE o.user.username = :username")
    Page<Order> findByUserUsernameWithItems(@Param("username") String username, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.items i JOIN FETCH i.product")
    Page<Order> findAllWithItems(Pageable pageable);

    @Query("SELECT o.user.username FROM Order o WHERE o.id = :id")
    Optional<String> findOwnerUsernameById(@Param("id") Long id);
}