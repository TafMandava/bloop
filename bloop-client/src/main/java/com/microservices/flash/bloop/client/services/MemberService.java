package com.microservices.flash.bloop.client.services;

import com.microservices.flash.bloop.client.exceptions.MemberNotFoundException;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.enums.AuthenticationType;

public interface MemberService {

    public Member findByEmail(String email);
    public boolean isEmailUnique(String email);
    public void registerMember(Member member);
    public void save(Member formMember);
    public void update(Member formMember);
    public boolean verify(String verificationCode);
    /**
     * Check if the AuthenticationType is different than the given Member's AuthenticationType
     */   
    public void updateAuthenticationType(Member member, AuthenticationType authenticationType);
    public void addMemberUponOAuthLogin(String name, String email, AuthenticationType authenticationType);
    /**
     * Return the generated token that will be used in the email content in the URL of the reset password link 
     */
    public String updateResetPasswordToken(String email) throws MemberNotFoundException;
    public  Member findByResetPasswordToken(String resetPasswordToken);
    public void updatePassword(String resetPasswordToken, String newPassword) throws MemberNotFoundException;
    
}
