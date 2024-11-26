package com.shoeb.full.controllers;


import com.shoeb.full.entities.User;
import com.shoeb.full.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/user/dashboard")
    public String dashboard(){
        System.out.println("Dashboard");
        return "user/dashboard";
    }


    @GetMapping("/user/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String profile(Authentication authentication){
        return "user/profile";
    }

    @GetMapping("/admin/students")
    @PreAuthorize("hasRole('ADMIN')")
    public String students(Model model, Authentication authentication){
        List<User> student = userService.getAllUsers();
        model.addAttribute("student", student);
        return "user/students";
    }


}
