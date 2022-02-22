package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface IUserService {

    @Transactional
    User createUser(UserDto userDto) throws UserAlreadyExistsException;

    @Transactional(readOnly = true)
    User getUserByEmail(String email) throws AppException;
}
