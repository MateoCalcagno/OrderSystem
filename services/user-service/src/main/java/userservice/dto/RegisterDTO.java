package userservice.dto;

import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank @Size(min = 3, max = 20)
    private String username;

    @NotBlank @Size(min = 6)
    private String password;

    @NotBlank @Email
    private String email;

    @NotBlank @Pattern(regexp = "\\d+")
    private String dni;

    @NotBlank @Size(min = 2)
    private String firstName;

    @NotBlank @Size(min = 2)
    private String lastName;

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
