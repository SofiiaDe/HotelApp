package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserService {

    @Transactional
    User createUser(UserDto userDto) throws UserAlreadyExistsException;

    @Transactional(readOnly = true)
    UserDto getUserByEmail(String email) throws AppException;

    @Transactional(readOnly = true)
    List<UserDto> getUsersByIds(List<Integer> ids) throws AppException;

    @Transactional(readOnly = true)
    UserDto getUserById(int id) throws AppException;
}
