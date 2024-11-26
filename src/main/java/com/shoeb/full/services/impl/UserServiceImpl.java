package com.shoeb.full.services.impl;

import com.shoeb.full.entities.User;
import com.shoeb.full.repositories.UserRepo;
import com.shoeb.full.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public UserRepo userRepo;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled((user.isEnabled()));
        userRepo.save(user);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
            User user = userRepo.findByEmail(email).orElse(null);
            return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users;
    }
}
