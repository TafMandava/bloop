package com.microservices.flash.bloop.client.utilities;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.microservices.flash.bloop.client.security.MemberUserDetails;
import com.microservices.flash.bloop.client.security.oauth.MemberOAuth2User;
import com.microservices.flash.bloop.common.data.configs.MailServerConfigData;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.entities.setttings.EmailSettingBag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberAccountUtil {

    public static void sendVerificationEmail(HttpServletRequest request, Member member, MailServerConfigData mailServerConfigData, EmailSettingBag emailTemplateSettings) throws MessagingException, UnsupportedEncodingException {

        JavaMailSenderImpl javaMailSender = MailUtil.prepareMailSender(mailServerConfigData);

        String toAddress = member.getEmail();
        String subject = emailTemplateSettings.getCustomerVerifySubject();
        String content = emailTemplateSettings.getCustomerVerifyContent();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(mailServerConfigData.getMailFrom(), mailServerConfigData.getMailSenderName());
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);

        content = content.replace("[[name]]", member.getFullname());

        String verificationUrl = String.format("%s/verification?code=%s", MailUtil.getSiteUrl(request), member.getVerificationCode());

        content = content.replace("[[URL]]", verificationUrl);

        /**
         * Set content for the email message 
         *    Stating that the email format will be in HTML - true
         */
        mimeMessageHelper.setText(content, true);

        javaMailSender.send(mimeMessage);

        log.debug("To Address '{}'", toAddress);
        log.debug("Verification URL '{}'", verificationUrl);
    }

    /**
     * Get the email of the currently Authenticated Member
     *     There are several ways for a Member to login
     */
    public static String retrieveAuthenticatedMemberEmail(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
            return principal.getName();
        } else if(principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            MemberOAuth2User memberOAuth2User = (MemberOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            return memberOAuth2User.getEmail();
        }
        return null;
    }

    public static void updateAuthenticatedMemberName(Member member, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
            MemberUserDetails memberUserDetails = retrieveMemberUserDetailsObject(principal);
            Member authenticatedMember = memberUserDetails.getMember();
            authenticatedMember.setFirstName(member.getFirstName());
            authenticatedMember.setLastName(member.getLastName());
            
        } else if(principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            MemberOAuth2User memberOAuth2User = (MemberOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            memberOAuth2User.setFullname(member.getFullname());
        }
    }
    
    public static MemberUserDetails retrieveMemberUserDetailsObject(Principal principal) {

        if(principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            return (MemberUserDetails) token.getPrincipal();
        } else if(principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            return (MemberUserDetails) token.getPrincipal();
        }

        return null;

    }


    public static void sentEmail(String resetPasswordUrl, String email, MailServerConfigData mailServerConfigData) throws UnsupportedEncodingException, MessagingException {
        JavaMailSenderImpl javaMailSender = MailUtil.prepareMailSender(mailServerConfigData);

        String toAddress = email;
        String subject = "Here is the link to reset your password";

        String content = "<p>Dear Member,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password.</p>"
                    + "<p><a href=\"" + resetPasswordUrl + "\">Change password</a></p>"
                    + "<br>"
                    + "<p>Please ignore this email if you did not request a password change</p>";


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(mailServerConfigData.getMailFrom(), mailServerConfigData.getMailSenderName());
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);

        /**
         * Set content for the email message 
         *    Stating that the email format will be in HTML - true
         */
        mimeMessageHelper.setText(content, true);

        javaMailSender.send(mimeMessage);                    
    }    
    
}
