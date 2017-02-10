package com.feldman.blazej.services.impl;

import com.feldman.blazej.model.User;
import com.feldman.blazej.repository.UserRepository;
import com.feldman.blazej.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        User newUser = userRepository.getOne(user.getUserId());
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setUserLogin(user.getUserLogin());
        newUser.setUserId(user.getUserId());
        userRepository.delete(user);
        userRepository.save(newUser);
    }

    @Override
    public User findUserByNameAndSurname(String name, String surname) {
        return userRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public User findUserByUserLoginAndPassword(String mail, String password) {
        return userRepository.findByUserLoginAndPassword(mail, password);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUserLogin(String userLogin) {
        return userRepository.findByUserLogin(userLogin);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
