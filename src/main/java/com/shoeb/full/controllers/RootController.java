package com.shoeb.full.controllers;


import com.shoeb.full.entities.User;
import com.shoeb.full.helpers.Helper;
import com.shoeb.full.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class RootController {
    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @ModelAttribute
    public void addLoggedInUserInfo(Model model, Authentication authentication){
        if (authentication==null){
            return;
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);
        logger.info("User : {}", user);
        logger.info("User Role : {}", user.getRoleList());
        logger.info(String.valueOf(user));
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);
    }
}