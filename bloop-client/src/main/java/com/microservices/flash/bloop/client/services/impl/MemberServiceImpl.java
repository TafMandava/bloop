package com.microservices.flash.bloop.client.services.impl;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.microservices.flash.bloop.client.exceptions.MemberNotFoundException;
import com.microservices.flash.bloop.client.repositories.MemberRepository;
import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.enums.AuthenticationType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public boolean isEmailUnique(String email) {

        Member memberByEmail = memberRepository.findByEmail(email);

        return ObjectUtils.isEmpty(memberByEmail);

    }

    @Override
    public void registerMember(Member member) {

        encodePassword(member);
        member.setEnabled(false);
        /**
         * Set the default authentication type to AuthenticationType.DATABASE for database logins
         */
        member.setAuthenticationType(AuthenticationType.DATABASE);

        String verificationCode = RandomString.make(64);
        member.setVerificationCode(verificationCode);

        log.debug("Verification code '{}'", verificationCode);

        memberRepository.save(member);

    }

    @Override
    public void save(Member formMember) {
        Member dbMember = memberRepository.findById(formMember.getId()).get();

        if(!formMember.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(formMember.getPassword());
            dbMember.setPassword(encodedPassword);
        }

        dbMember.setFirstName(formMember.getFirstName());
        dbMember.setLastName(formMember.getLastName());
        dbMember.setPhoneNumber(formMember.getPhoneNumber());

        memberRepository.save(dbMember);
    }    

    @Override
    public void update(Member formMember) {
        Member dbMember = memberRepository.findById(formMember.getId()).get();

        if(dbMember.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if(!formMember.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(formMember.getPassword());
                dbMember.setPassword(encodedPassword);
            }
        }

        dbMember.setFirstName(formMember.getFirstName());
        dbMember.setLastName(formMember.getLastName());
        dbMember.setPhoneNumber(formMember.getPhoneNumber());

        memberRepository.save(dbMember);
    }

    @Override
    public boolean verify(String verificationCode) {

        Member member = memberRepository.findByVerificationCode(verificationCode);

        if(ObjectUtils.isEmpty(member) || member.isEnabled()) {
            return false;
        } else {
            memberRepository.enable(member.getId());
            return true;
        }
    
    }

    /**
     * Check if the AuthenticationType is different than the given Member's AuthenticationType
     */  
    @Override 
    public void updateAuthenticationType(Member member, AuthenticationType authenticationType) {

        if(!member.getAuthenticationType().equals(authenticationType)) {
            memberRepository.updateAuthenticationType(authenticationType, member.getId());
        }

    }

    @Override
    public void addMemberUponOAuthLogin(String name, String email, AuthenticationType authenticationType) {
        /**
         * The member must update the empty values inorder to continue shopping 
         *     Address Line 2 is not mandatory
         */
        Member member = new Member();
         
        member.setFirstName(name);
        setName(name, member);

        member.setEmail(email);
        member.setEnabled(true);
        member.setAuthenticationType(authenticationType);
        member.setPassword("");

        memberRepository.save(member);

    }
    
    /**
     * Return the generated token that will be used in the email content in the URL of the reset password link 
     */
    @Override
    public String updateResetPasswordToken(String email) throws MemberNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(!ObjectUtils.isEmpty(member)) {
            String resetPasswordToken = RandomString.make(30);
            member.setResetPasswordToken(resetPasswordToken);
            memberRepository.save(member);

            return resetPasswordToken;
        }
        throw new MemberNotFoundException(String.format("Could not find any member with the email '%s'", email));
    }

    @Override
    public  Member findByResetPasswordToken(String resetPasswordToken) {
        return memberRepository.findByResetPasswordToken(resetPasswordToken);
    }
    
    @Override
    public void updatePassword(String resetPasswordToken, String newPassword) throws MemberNotFoundException {
        Member member = memberRepository.findByResetPasswordToken(resetPasswordToken);

        if (ObjectUtils.isEmpty(member)) {
            throw new MemberNotFoundException(String.format("Could not find member. Invalid token '%s'", resetPasswordToken));
        }

        member.setPassword(newPassword);
        member.setResetPasswordToken(null);
        encodePassword(member);

        memberRepository.save(member);

    }   
    
    private void encodePassword(Member member) {

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

    }

    private void setName(String name, Member member) {
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            member.setFirstName(name);
            member.setLastName("");
        } else {
            String firstName = nameArray[0];
            member.setFirstName(nameArray[0]);

            String lastName = name.replaceFirst(firstName + " ", "");
            member.setLastName(lastName);
        }
    }
    
}
