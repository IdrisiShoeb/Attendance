package com.shoeb.full.controllers;

import com.shoeb.full.entities.User;
import com.shoeb.full.entities.UserRole;
import com.shoeb.full.forms.UserForm;
import com.shoeb.full.helpers.Message;
import com.shoeb.full.repositories.UserRepo;
import com.shoeb.full.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {
    private UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

//    @Autowired
//    private UserRepo userRepo;
//
//
//    private PasswordEncoder passwordEncoder;

//    public PageController(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }


    @GetMapping("/")
    public String index(){
        System.out.println("Index");
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(){
        System.out.println("Home");
        return "home";
    }

//    @GetMapping("/t")
//    public void t(){
//        User user = new User();
//        user.setEmail("test1@test.com");
//        user.setName("Test1");
//        user.setEnabled(true);
//        user.setPassword(passwordEncoder.encode("test"));
//        userRepo.save(user);
//    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession httpSession){
        System.out.println("register post");

        System.out.println(userForm);

        if (rBindingResult.hasErrors())
            return "register";

//        User user = User.builder()
//                .name(userForm.getName())
//                .email(userForm.getEmail())
//                .rollNo(userForm.getRollNo())
//                .phoneNo(userForm.getPhoneNo())
//                .password(userForm.getPassword())
//                .enabled(true)
//                .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setRollNo(userForm.getRollNo());
        user.setPhoneNo(userForm.getPhoneNo());
        user.setEnabled(true);

        if (userForm.getRoles() == null || userForm.getRoles().isEmpty()) {
            user.getRoleList().add(UserRole.USER);
        } else {
            user.getRoleList().addAll(userForm.getRoles());
        }

        userService.addUser(user);

        Message message = Message.builder().content("Registration Successful.").type("green").build();
        httpSession.setAttribute("message", message );
        return "redirect:/register";
    }

    @GetMapping("/403")
    public String forbidden() {
        return "403"; // This refers to src/main/resources/templates/403.html
    }

}
