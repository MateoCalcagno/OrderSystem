package ordersystem.dto;

public class UserResponseDTO {
    private String username;
    private String role;

    public UserResponseDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }
}