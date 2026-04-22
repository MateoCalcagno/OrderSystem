package userservice.mapper;

import userservice.model.User;
import userservice.model.Role;
import userservice.dto.UserResponseDTO;
import userservice.dto.RegisterDTO;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getUsername(), user.getRole());
    }

    public static User toEntity(RegisterDTO dto, String encodedPassword) {
        return new User(
            dto.getUsername(),
            encodedPassword,
            Role.USER,
            dto.getEmail(),
            dto.getDni(),
            dto.getFirstName(),
            dto.getLastName()
        );
    }
}
