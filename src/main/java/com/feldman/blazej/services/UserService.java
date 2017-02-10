package com.feldman.blazej.services;

import com.feldman.blazej.model.User;

import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
public interface UserService {

    void addUser(User user);

    void updateUser(User user);

    User findUserByNameAndSurname(String name, String surname);

    User findUserByUserLoginAndPassword(String mail, String password);

    User findUserByEmail(String email);

    User findUserByUserLogin(String userLogin);

    List<User> getAllUsers();
}
