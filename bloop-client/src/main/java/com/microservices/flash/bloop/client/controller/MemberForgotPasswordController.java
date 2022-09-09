package com.microservices.flash.bloop.client.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.microservices.flash.bloop.client.exceptions.MemberNotFoundException;
import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.client.utilities.MailUtil;
import com.microservices.flash.bloop.client.utilities.MemberAccountUtil;
import com.microservices.flash.bloop.common.data.configs.MailServerConfigData;
import com.microservices.flash.bloop.common.data.entities.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberForgotPasswordController {

    private final MemberService memberService;

    private final MailServerConfigData mailConfigData;

    @GetMapping("/forgot-password")
    public String requestForm() {
        return "member/forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String requestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");

        try{
            String resetPasswordToken = memberService.updateResetPasswordToken(email);
            log.debug("Reset password token '{}' generated for member with email '{}'", resetPasswordToken, email);

            String resetPasswordUrl = String.format("%s/reset-password?token=%s", MailUtil.getSiteUrl(request), resetPasswordToken);
            MemberAccountUtil.sentEmail(resetPasswordUrl, email, mailConfigData);

            model.addAttribute("message", "A reset password link has been sent to your email. Please check your email.");

        } catch(MemberNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Sending of email failed");
            e.printStackTrace();
        }
        
        return "member/forgot-password-form";
    }

    /**
     * Show the reset password form when the member clicks the password link sent via email 
     *    Access the value of the token parameter in the URL link
     */
    @GetMapping("/reset-password")
    public String resetPasswordForm(@Param("token") String token, Model model) {

        log.debug("Reset password token '{}'", token);

        Member member = memberService.findByResetPasswordToken(token);

        if(!ObjectUtils.isEmpty(member)) {
            model.addAttribute("token", token);
            return "member/reset-password-form";

        } else {
            model.addAttribute("pageTitle", "Invalid token");
            model.addAttribute("message", "Invalid token");
            return "message";
        }

    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(HttpServletRequest request, Model model) {

        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        log.debug("Reset password token '{}'", token);

        try {
            memberService.updatePassword(token, newPassword);
            model.addAttribute("pageTitle", "Password Reset");
            model.addAttribute("title", "Password Reset");
            model.addAttribute("message", "You have successfully changed your password");

        } catch (MemberNotFoundException e) {
            model.addAttribute("pageTitle", "Invalid token");
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();

        }

        return "message";

    }
    
}
