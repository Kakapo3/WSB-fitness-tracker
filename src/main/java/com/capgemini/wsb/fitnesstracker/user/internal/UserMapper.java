package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user != null ? user.getId() : null,
                user != null && user.getFirstName() != null ? user.getFirstName() : null,
                user != null && user.getLastName() != null ? user.getLastName() : null,
                user != null ? user.getBirthdate() : null,
                user != null && user.getEmail() != null ? user.getEmail() : null
        );
    }

    public User toEntity(UserDto userDto) {
        return new User(
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.birthdate(),
                        userDto.email());
    }

    public UserSimpleDto toSimpleDto(User user) {
        return new UserSimpleDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public UserEmailDto toEmailDto(User user) {
        return new UserEmailDto(
                user.getId(),
                user.getEmail()
        );
    }

}
