package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserService {

    User createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserDto getUserByEmail(String email);

    List<UserDto> getUsersByIds(List<Integer> ids) throws AppException;

    UserDto getUserById(int id);

    List<UserDto> getAllUsers() throws AppException;
}
