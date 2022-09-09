package com.microservices.flash.bloop.client.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class MemberOAuth2UserService extends DefaultOAuth2UserService {

    /**
     * The method loadUser is invoked by the Spring Security OAuth 2.0 Client upon successful OAuth2 Authorization
     * 
     * Return an instance of OAuth2 User
     */    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        /**
         * The client name is "google" or "facebook" specified in the application.yml file
         *    The application will be able to identify which type of the client
         */
        String clientName = userRequest.getClientRegistration().getClientName();
        return new MemberOAuth2User(oAuth2User, clientName);
    }
    
}
