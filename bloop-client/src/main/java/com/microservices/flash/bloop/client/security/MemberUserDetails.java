package com.microservices.flash.bloop.client.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.microservices.flash.bloop.common.data.entities.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberUserDetails implements UserDetails {

    /**
     * Wrap an instance of the Member class 
     */
    private Member member;
    

    /**
     * A Member does not have any specific Roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * Return the value of the Member's password 
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * Return the Member's email because we are using the email as the username. This is defined in the SecurityConfig class 
     */
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    /**
     * Return true meaning that the account is non expired 
     */    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Return true meaning that the account is non locked 
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Return true meaning tha the credentials are non expired 
     */    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Return the value of the Member's enabled property 
     */
    @Override
    public boolean isEnabled() {
        return member.isEnabled();
    }

    /**
     * Return a Member's full name 
     */
    public String getFullname() {
        return member.getFullname();
    }

    public void setFirstName(String firstName) {
        this.member.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.member.setLastName(lastName);
    }
    
}

