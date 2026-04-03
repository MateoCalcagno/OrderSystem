package ordersystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "Mínimo 4 caracteres")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}