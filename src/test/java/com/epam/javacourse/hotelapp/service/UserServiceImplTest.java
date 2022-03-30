package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.exception.UserAlreadyExistsException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.impl.UserServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.epam.javacourse.hotelapp.dto.UserDto;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Test
    void testGetAllUsers_whenCalled_callsRepo() {
        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);

        userService.getAllUsers();
        verify(userRepositoryMock, times(1)).findAll();
    }

//    @Test
//    void testCreate_whenRepoThrows_throwsException() {
////        doThrow(new UserAlreadyExistsException()).when(userRepositoryMock).save(any());
//        doReturn(null).when(userRepositoryMock).save(UserMapper.mapFromDto(getUser()));
//        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);
//
//        Assertions.assertThrowsExactly(UserAlreadyExistsException.class, () -> userService.createUser(new UserDto()));
//    }
//
//    @Test
//    void testCreate_whenRepoThrows_ShowExceptionMessage() {
//        String messageNotToGet = "aaaaa";
//        String email = "email@com";
//
//        when(userRepositoryMock.findUserByEmail(email))
//                .thenThrow(new NoSuchElementFoundException("There is an account with the following email address: " + email));
//
////        doThrow(new UserAlreadyExistsException("There is an account with the following email address: " + email)).when(userRepositoryMock).save(any(User.class));
//
//        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);
//
//        try {
//            userService.createUser(new UserDto());
//        } catch (UserAlreadyExistsException ex) {
//            assertEquals("There is an account with the following email address: " + email, ex.getMessage());
//            assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown UserAlreadyExistsException");
//    }

    @Test
    void testCreate_whenCalled_callsRepo() throws UserAlreadyExistsException {
        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);

        userService.createUser(new UserDto());
        verify(userRepositoryMock, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserByEmail_whenCalled_RepoCalled() {
        String email = "aaa@bbb.ccc";
        User user = UserMapper.mapFromDto(getUser());
        when(userRepositoryMock.findUserByEmail(any())).thenReturn((user));

        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);
        userService.getUserByEmail(email);

        verify(userRepositoryMock, times(1)).findUserByEmail(email);
    }

    @Test
    void testGetUserByEmail_whenRepoThrows_ShowDbExceptionMessage() {
        String messageNotToGet = "aaaaa";
        String email = "aaabbb.ccc";
        when(userRepositoryMock.findUserByEmail(email)).thenThrow(new NoSuchElementFoundException(messageNotToGet));
        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);

        try {
            userService.getUserByEmail(email);
        } catch (NoSuchElementFoundException ex) {
            assertEquals("Can't retrieve user by email", ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetUserById_whenCalled_RepoCalled() {
        int userId = 112567890;
        User user = UserMapper.mapFromDto(getUser());
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(user));

        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);
        userService.getUserById(userId);

        verify(userRepositoryMock, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_whenRepoThrows_throwsException() {
        int userId = 112567890;

        when(userRepositoryMock.findById(userId)).thenThrow(new NoSuchElementFoundException("Can't get user with id = " + userId));
        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);

        Assertions.assertThrowsExactly(NoSuchElementFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testGetUserById_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int userId = 112567890;

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());
        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);

        try {
            userService.getUserById(userId);
        } catch (NoSuchElementFoundException ex) {
            assertEquals("Can't retrieve user with id = " + userId, ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown NoSuchElementFoundException");
    }

    @Test
    void testGetUserById_whenCalled_returnsCorrectUser() {
        int userId = 112567890;
        UserDto expectedUser = getUser();

        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(UserMapper.mapFromDto(expectedUser)));
        UserServiceImpl userService = new UserServiceImpl(userRepositoryMock);

        UserDto result = userService.getUserById(userId);

        assertEquals(expectedUser.getFirstName(), result.getFirstName());
        assertEquals(expectedUser.getLastName(), result.getLastName());
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getPassword(), result.getPassword());
        assertEquals(expectedUser.getCountry(), result.getCountry());
        assertEquals(expectedUser.getRole(), result.getRole());
    }

    private UserDto getUser() {
        UserDto user = new UserDto();
        user.setFirstName("UserFirstName");
        user.setLastName("UserLastName");
        user.setEmail("aaa@bbb.ccc");
        user.setPassword("password");
        user.setCountry("Ukraine");
        user.setRole("client");

        return user;
    }
}
