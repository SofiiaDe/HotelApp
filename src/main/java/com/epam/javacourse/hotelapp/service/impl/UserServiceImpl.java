package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.UserDto;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
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

    private final UserRepository userRepository;

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
    public UserDto getUserByEmail(String email) {
        UserDto userDto;
        try {
            userDto = UserMapper.mapToDto(userRepository.findUserByEmail(email));
        } catch (NoSuchElementFoundException exception) {
            throw new NoSuchElementFoundException("Can't retrieve user by email", exception);
        }
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new NoSuchElementFoundException("Can't retrieve user with id = " + id));
        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByIds(List<Integer> ids) {
        List<User> users = userRepository.findUsersByIds(ids);
        List<UserDto> result = new ArrayList<>();
        users.forEach(x -> result.add(UserMapper.mapToDto(x)));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        users.forEach(x -> result.add(UserMapper.mapToDto(x)));
        return result;
    }
}
