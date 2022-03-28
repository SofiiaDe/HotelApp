package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.epam.javacourse.hotelapp.dto.UserDto;


@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private IUserService userService;

    @Test
    void testCreateUser() throws UserAlreadyExistsException {
        when(this.userRepository.existsByEmail(any())).thenReturn(true);

        UserDto userDTO = new UserDto();
        userDTO.setEmail("vi.v@gmail.com");

        User result = userService.createUser(userDTO);
//        Assertions.assertEquals(result);
    }
}
