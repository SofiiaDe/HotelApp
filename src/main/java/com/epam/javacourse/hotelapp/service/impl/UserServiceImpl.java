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

    @Autowired
    private UserRepository userRepository;

    UserMapper userMapper = new UserMapper();

    @Override
    @Transactional(readOnly = true)
    public User createUser(UserDto userDto) throws UserAlreadyExistsException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setCountry(userDto.getCountry());
        user.setRole(userDto.getRole());

        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        return userRepository.findUserByEmail(email) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) throws AppException {
        return userMapper.mapToDto(userRepository.findUserByEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByIds(List<Integer> ids) throws AppException {
        List<User> users = userRepository.findUsersByIds(ids);
        List<UserDto> result = new ArrayList<>();
        users.forEach(x -> result.add(userMapper.mapToDto(x)));
        return result;
    }

}
