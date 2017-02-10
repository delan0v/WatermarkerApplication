package com.feldman.blazej.presenter;

import com.feldman.blazej.model.User;
import com.feldman.blazej.services.UserService;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.newUser.RegexPattern;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
@UIScope
@Component
public class UserPresenter {

    @Autowired
    private UserService userService;

    public void addNewUser(User user) {
        userService.addUser(user);
    }

    public void updateThisUser(User user) {
        userService.updateUser(user);
    }

    public User searchUserByNameAndSurname(String name, String surname) {
        return userService.findUserByNameAndSurname(name, surname);
    }

    public User searchUserByLogin(String userLogin) {
        return userService.findUserByUserLogin(userLogin);
    }

    public User searchUserByLoginAndPassword(String login, String password) {
        return userService.findUserByUserLoginAndPassword(login, password);
    }

    public User searchUserByMail(String email) {
        return userService.findUserByEmail(email);
    }

    public List<User> searchAllUsers() {
        return userService.getAllUsers();
    }

    public String checkUser(String userLogin, String email, String name, String surname, String password) {
        if (searchUserByLogin(userLogin) != null) {
            return "Klient o podanym loginie już istnieje";
        } else if (searchUserByMail(email) != null) {
            return "Podany e-mail został już wykorzystany";
        } else {
            if (RegexPattern.checkName(name) == false) {
                return "Podaj swoje prawdziwe imię. Pierwsza litera powinna być duża, reszta mała";
            } else if (RegexPattern.checkSurname(surname) == false) {
                return "Podaj swoje prawdziwe nazwisko. Pierwsza litera powinna być duża, reszta mała";
            } else if (RegexPattern.checkLogin(userLogin) == false) {
                return "Login powinien składać się wyłącznie z małych i dużych liter oraz cyfr. Znaki specjalne są niedozwolone. Minimalna liczba znaków = 6, maksymalna =15";
            } else if (RegexPattern.checkMail(email) == false) {
                return "Podaj rzeczywisty mail. Nie zapomnij o znaku małpy!";
            } else if (RegexPattern.checkPassword(password) == false) {
                return "Hasło powinno składać się od 6 do 15 znaków";
            } else {
                return addUserToDatabase(userLogin, email, name, surname, password);
            }
        }
    }
    public String addUserToDatabase(String userLogin, String email, String name, String surname, String password) {
        User user = new User();
        user.setUserId(null);
        user.setUserLogin(userLogin);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        try {
            addNewUser(user);
            return "";
        } catch (Exception e) {
            return "Podałeś złe dane";
        }
    }
}

