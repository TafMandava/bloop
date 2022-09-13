package com.microservices.flash.bloop.client.services.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservices.flash.bloop.client.services.WordCensorshipService;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties(prefix = "flash.bloop", ignoreUnknownFields = true)
@Service
public class WordCensorshipServiceImpl implements WordCensorshipService {

    public static final String MESSAGE_PATH_V1 = "/api/v1/message";

    private String wordCensorshipServiceHost;

    public void setWordCensorshipServiceHost(String wordCensorshipServiceHost) {
        this.wordCensorshipServiceHost = wordCensorshipServiceHost;
    }

    private final RestTemplate restTemplate;

    public WordCensorshipServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public MessageDto censorWords(MessageDto message) {

        try {
            log.debug("Posting to callback url");
            return restTemplate.postForObject(wordCensorshipServiceHost + MESSAGE_PATH_V1, message, MessageDto.class);
        } catch (Throwable throwable) {
            log.error("Error performing callback for word censorship"  + message.getText(), throwable);
        }
        return message;
    }
    
}
