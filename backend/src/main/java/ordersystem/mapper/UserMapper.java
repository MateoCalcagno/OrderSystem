package ordersystem.mapper;

import ordersystem.model.User;
import ordersystem.dto.UserResponseDTO;
import ordersystem.dto.RegisterDTO;
import ordersystem.model.Role;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
            user.getUsername(),
            user.getRole()
        );
    }

    public static User toEntity(RegisterDTO dto, String encodedPassword) {
        return new User(
            dto.getUsername(),
            encodedPassword,
            Role.USER, // rol por defecto
            dto.getEmail(),
            dto.getDni(),
            dto.getFirstName(),
            dto.getLastName()
        );
    }
}