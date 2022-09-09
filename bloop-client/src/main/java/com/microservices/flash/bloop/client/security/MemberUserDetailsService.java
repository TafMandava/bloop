package com.microservices.flash.bloop.client.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import com.microservices.flash.bloop.client.repositories.MemberRepository;
import com.microservices.flash.bloop.common.data.entities.Member;

public class MemberUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * This method will be called by Spring Security when it is performing Authentication 
     *     Username equals email because we specified the email as the username in the SecurityConfig class
     */    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(!ObjectUtils.isEmpty(member)) {
            return new MemberUserDetails(member);
        }
        /**
         * The class UsernameNotFoundException is provided by Spring Security
         */        
        throw new UsernameNotFoundException(String.format("Could not find a member with the email '%s'", email));
    }
    
}