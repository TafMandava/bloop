package com.microservices.flash.bloop.client.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberOAuth2User implements OAuth2User {

    /**
     * Reference OAuth2User 
     */
    private final OAuth2User oAuth2User;

    /**
     * The client name is "google" or "facebook" specified in the application.yml file
     *    The application will be able to identify which type of the client
     */
    private final String clientName;

    private String fullname;

    /**
     * Return attributes 
     */
    @Override
    public Map<String, Object> getAttributes() {
        return  oAuth2User.getAttributes();
    }

    /**
     * Return Authorities 
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    /**
     * Return the value of the attribute name 
     */
    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    /**
     * Return the value of the attribute email 
     */
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }    

    /**
     * Return an OAuth2User's full name 
     */
    public String getFullname() {
        return ObjectUtils.isEmpty(fullname) ? oAuth2User.getAttribute("name") : fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
}
