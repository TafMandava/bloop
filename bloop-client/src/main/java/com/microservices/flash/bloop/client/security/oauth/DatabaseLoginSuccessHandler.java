package com.microservices.flash.bloop.client.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.microservices.flash.bloop.client.security.MemberUserDetails;
import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.enums.AuthenticationType;

@Component
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        /**
         * We can access the Authentication object from which we can get the principal object
         *    because in the MemberUserDetailsService we return a new instance of MemberUserDetails we can cast the return object getPrincipal() 
         */                
        MemberUserDetails memberUserDetails = (MemberUserDetails) authentication.getPrincipal();
        Member member = memberUserDetails.getMember();
        
        /**
         * Update the AuthenticationType 
         */
        memberService.updateAuthenticationType(member, AuthenticationType.DATABASE);

        super.onAuthenticationSuccess(request, response, authentication);
    }
    
}
