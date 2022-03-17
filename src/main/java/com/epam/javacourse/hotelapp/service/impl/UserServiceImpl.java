package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(UserDto userDto) throws UserAlreadyExistsException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with the following email address: "
                    + userDto.getEmail());
        }
        return userRepository.save(UserMapper.mapFromDto(userDto));
    }

    private boolean emailExist(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) throws AppException {
        return UserMapper.mapToDto(userRepository.findUserByEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(int id) throws AppException {
        return UserMapper.mapToDto(userRepository.getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByIds(List<Integer> ids) throws AppException {
        List<User> users = userRepository.findUsersByIds(ids);
        List<UserDto> result = new ArrayList<>();
        users.forEach(x -> result.add(UserMapper.mapToDto(x)));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() throws AppException {
        List<User> users = userRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        users.forEach(x -> result.add(UserMapper.mapToDto(x)));
        return result;
    }
}
