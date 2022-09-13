package com.microservices.flash.bloop.censorshipservice.services;

import com.microservices.flash.bloop.common.data.dtos.MessageDto;

public interface WordCensorshipService {

    MessageDto censorSensitiveWords(MessageDto message);
    
}
