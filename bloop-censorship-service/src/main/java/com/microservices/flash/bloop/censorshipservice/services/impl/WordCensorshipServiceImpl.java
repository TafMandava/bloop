package com.microservices.flash.bloop.censorshipservice.services.impl;

import java.util.List;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.microservices.flash.bloop.censorshipservice.domain.SensitiveWord;
import com.microservices.flash.bloop.censorshipservice.repositories.SensitiveWordRepository;
import com.microservices.flash.bloop.censorshipservice.services.WordCensorshipService;
import com.microservices.flash.bloop.common.data.entities.Message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WordCensorshipServiceImpl implements WordCensorshipService {

    private final SensitiveWordRepository sensitiveWordRepository;

    @Override
    public Message censorSensitiveWords(Message message) {

        StringBuilder stringBuilder = new StringBuilder();
        StringTokenizer stringTokenizer = new StringTokenizer(message.getText());

        while (stringTokenizer.hasMoreTokens()) {

            List<SensitiveWord> sensitiveWords = sensitiveWordRepository.findByTextIgnoreCaseContaining(stringTokenizer.nextToken());
            if(!ObjectUtils.isEmpty(sensitiveWords)) {
                stringBuilder.append( censorMessage( stringTokenizer.nextToken() ) );
            } 
            
            stringBuilder.append(message);
 
            if (stringTokenizer.hasMoreTokens()) {
                stringBuilder.append(" ");
            }

        }

        message.setText(stringBuilder.toString());

        return message;
    }

    private String censorMessage(String text) {
        String replacement = "*";
        for(int index = 1; index < text.length(); index++){
            replacement += replacement;
        }
        return replacement; 
    }
    
}
