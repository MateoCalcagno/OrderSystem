package ordersystem.dto;

import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El username debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d+", message = "El DNI solo debe contener números")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre es demasiado corto")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido es demasiado corto")
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