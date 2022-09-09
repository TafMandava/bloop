package com.microservices.flash.bloop.client.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.enums.AuthenticationType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * For retrieving member by email
     */
    @Autowired
    private MemberService memberService;

    /**
     * The method will be invoked by Spring Security upon successful authentication 
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        /**
         * We can access the Authentication object from which we can get the principal object
         *    because in the MemberOAuth2UserService we return a new instance of MemberOAuth2User we can cast the return object getPrincipal() 
         */
        MemberOAuth2User memberOAuth2User = (MemberOAuth2User) authentication.getPrincipal();

        String name = memberOAuth2User.getName();
        String email = memberOAuth2User.getEmail();
        String clientName = memberOAuth2User.getClientName();

        /**
         * Ensure that the OAuth2LoginSuccessHandle is invoked
         */
        log.debug("OAuth2LoginSuccessHandler '{}' '{}'", name, email);
        log.debug("Client name '{}'", clientName);


        /**
         * Get Member by email 
         */
        Member member = memberService.findByEmail(email);

        AuthenticationType authenticationType = determineAuthenticationType(clientName);

        /**
         * Is Member found?
         *     Yes
         *         Update Authentication Type
         *             Continue with work flow
         * 
         *     No
         *         Add new Member Account
         *             Continue with work flow
         */
        if(ObjectUtils.isEmpty(member)) {
            memberService.addMemberUponOAuthLogin(name, email, authenticationType);
        } else {

            /**
             * Update the OAuth2 SuccessHandler to pull Member First Name and Last Name from the database instead of name from OAuth2User
             */
            memberOAuth2User.setFullname(member.getFullname());
            memberService.updateAuthenticationType(member, authenticationType);

        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType determineAuthenticationType(String clientName) {
        if(clientName.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if(clientName.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        }
        return AuthenticationType.DATABASE;
    }
    
}
