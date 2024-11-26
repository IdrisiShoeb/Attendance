package com.shoeb.full.helpers;

import org.springframework.security.core.Authentication;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){
        System.out.println("Local email: " + authentication.getName());
        return authentication.getName();
    }
}
