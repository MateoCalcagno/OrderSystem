package ordersystem.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    // Relación: Un usuario puede tener muchos pedidos
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders;

    public User() {}

    public User(String username, String password, Role role,
                String email, String dni, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters y Setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}