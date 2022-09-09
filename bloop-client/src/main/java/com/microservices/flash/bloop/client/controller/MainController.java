package com.microservices.flash.bloop.client.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping
    public String homePage() {
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication) || authentication instanceof AnonymousAuthenticationToken ) {
            return "login";
        }
        /**
         * Else the member is authenticated 
         */
        return "redirect:/";
    }    

}
