package com.epam.javacourse.hotelapp.dao.interfaces;

import com.epam.javacourse.hotelapp.model.User;

import java.util.List;

public interface IUserDAO {

    List<User> findAllUsers() throws DBException;

    void createUser(User user) throws DBException;

    User findUserByEmail(String email) throws DBException;

    List<User> findUsersByIds(List<Integer> ids) throws DBException;

    User findUserById(int id) throws DBException;
}
