package com.deephire.Mappers;

import com.deephire.Dto.SignUpDto;
import com.deephire.Dto.UserDto;
import com.deephire.Models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convert User entity to UserDto.
    UserDto toUserDto(User user);

    // Convert SignUpDto to User, ignoring the password property.
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto userDto);
}
