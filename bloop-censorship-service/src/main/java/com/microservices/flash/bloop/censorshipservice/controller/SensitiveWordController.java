package com.microservices.flash.bloop.censorshipservice.controller;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.flash.bloop.censorshipservice.services.WordCensorshipService;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;
import com.microservices.flash.bloop.common.data.entities.Message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/message")
public class SensitiveWordController {

    private final  WordCensorshipService  wordCensorshipService;

    @PostMapping
    public ResponseEntity<MessageDto> censorWords(@NotNull @RequestBody MessageDto message) {
        return new ResponseEntity<MessageDto>(wordCensorshipService.censorSensitiveWords(message), HttpStatus.OK);
    }    
    
}
