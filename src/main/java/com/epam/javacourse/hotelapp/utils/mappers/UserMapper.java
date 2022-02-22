package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.model.User;

public class UserMapper implements DtoMapper<UserDto, User>{

    @Override
    public UserDto mapToDto(User user){
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setCountry(user.getCountry());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public User mapFromDto(UserDto dto){
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCountry(dto.getCountry());
        user.setRole(dto.getRole());
        return user;
    }
}
