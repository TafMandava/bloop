package com.microservices.flash.bloop.censorshipservice.services;

import com.microservices.flash.bloop.common.data.entities.Message;

public interface WordCensorshipService {

    Message censorSensitiveWords(Message message);
    
}
