package com.shoeb.full.helpers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
    public static void removeMsg(){
        try{
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        }catch (Exception e){
            System.out.println("Error in session helper: " + e);
            e.printStackTrace();
        }

    }
}
