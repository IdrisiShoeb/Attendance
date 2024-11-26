package com.shoeb.full.services;

import com.shoeb.full.entities.User;

import java.util.List;


public interface UserService {
    User getUserByEmail(String email);
    void addUser(User user);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
}
