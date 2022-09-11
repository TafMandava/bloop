package com.microservices.flash.bloop.client.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.client.services.SettingService;
import com.microservices.flash.bloop.client.utils.MemberAccountUtil;
import com.microservices.flash.bloop.common.data.configs.MailServerConfigData;
import com.microservices.flash.bloop.common.data.entities.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final SettingService settingService;

    private final MemberService memberService;

    private final MailServerConfigData mailServerConfigData;

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("pageTitle", "Member Registration");
        model.addAttribute("member", Member.builder().build());

        return "registration/registration-form";
        
    }

    /**
     * Spring will automatically the member object method paramater to the rest form register-form.html
     * Access the Spring MVC model to set the pageTitle
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/create-member")
    public String createMember(Member member, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {

        memberService.registerMember(member);
        MemberAccountUtil.sendVerificationEmail(request, member, mailServerConfigData, settingService.getMailSettings());

        model.addAttribute("pageTitle", "Registration completed successfully");

        return "registration/registration-success";
    }

    @GetMapping("/verification")
    public String verifyAccount(@Param("code") String code, Model model) {
        boolean verified = memberService.verify(code);

        return "registration/" + (verified ? "verification-success" : "verification-fail");
    }

    /**
     * A Member can login using an email and password stored in the database 
     *    We can get the principal from the HttpServletRequest
     *        Represents the currently authenticated User object
     * 
     *    Type of the principal object is different depending on whether we used Database Login and Social Media Login
     *        Facebook or Google Login 
     *            org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
     *        Database Login
     *            org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     *            
     *            If Remember Me is checked
     *                 HttpServletRequest request
     * 
     */
    @GetMapping("/account-details")
    public String accountDetails(Model model, HttpServletRequest request) {

        String principalType = request.getUserPrincipal().getClass().getName();

        log.debug("Principal Type '{}'", principalType);


        Member member = memberService.findByEmail(MemberAccountUtil.retrieveAuthenticatedMemberEmail(request));

        model.addAttribute("member", member);

        return "member/account-form";
    }

    @PostMapping("/update-member-details")
    public String updateMemberDetails(Model model, Member member, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        memberService.update(member);

        MemberAccountUtil.updateAuthenticatedMemberName(member, request);

        redirectAttributes.addFlashAttribute("message", "Your account details have been updated successfully");

        return "redirect:/account-details";
    }
        

}
