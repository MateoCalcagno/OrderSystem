package ordersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import ordersystem.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Esto nos permite buscar un usuario por su nombre de forma fácil
    Optional<User> findByUsername(String username);
}