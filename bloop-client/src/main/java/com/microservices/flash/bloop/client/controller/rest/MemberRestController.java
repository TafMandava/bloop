package com.microservices.flash.bloop.client.controller.rest;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.flash.bloop.client.services.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/members/check-email")
    public String checkDuplicateEmail(@Param("email") String email) {
        return memberService.isEmailUnique(email) ? "Ok" : "Duplicate";
    }    

}
