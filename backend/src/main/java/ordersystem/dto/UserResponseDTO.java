package ordersystem.dto;

import ordersystem.model.Role;

public class UserResponseDTO {
    private String username;
    private Role role;

    public UserResponseDTO(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() { return username; }
    public Role getRole() { return role; }
}