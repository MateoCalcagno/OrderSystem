package ordersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, message = "Mínimo 3 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public String getUsername(){ return username; }
    public String getPassword(){ return password; }

    public void setUsername(String username){ this.username = username; }
    public void setPassword(String password) { this.password = password; }
}