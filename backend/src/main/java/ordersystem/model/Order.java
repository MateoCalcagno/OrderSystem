package ordersystem.model;
 
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
 
import org.hibernate.annotations.CreationTimestamp;
 
@Entity
@Table(name = "orders")
public class Order {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // CascadeType.ALL: al guardar/borrar la Order, se propaga a sus items.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
 
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
 
    public Order() {}
 
    // El total ya no se guarda en BD — se calcula desde los items
    public BigDecimal getTotalPrice() {
        return items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
 
    public Long getId() { return id; }
 
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
 
    public LocalDateTime getCreatedAt() { return createdAt; }
 
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}