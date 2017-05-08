package com.feldman.blazej.repository;

import com.feldman.blazej.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameAndSurname(String name, String surname);

    User findByUserLoginAndPassword(String userLogin, String password);

    User findByEmail(String email);

    User findByUserLogin(String userLogin);
}