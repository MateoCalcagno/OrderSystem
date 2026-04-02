package ordersystem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "Mínimo 4 caracteres")
    private String password;

    private Role role; 

    public String getUsername(){ return username; }
    public Role getRole(){ return role; }
    public String getPassword(){ return password; }
    public void setPassword(String password) { this.password = password; }
    public void setUsername(String username){ this.username = username; }
    public void setRole(Role role) { this.role = role; }

}