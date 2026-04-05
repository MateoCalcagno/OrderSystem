package ordersystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne // Muchos pedidos pertenecen a un usuario
    @JoinColumn(name = "user_id") 
    @JsonBackReference
    private User user;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Order() {}

    public Order(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}