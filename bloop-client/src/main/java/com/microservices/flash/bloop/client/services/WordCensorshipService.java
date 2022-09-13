package com.microservices.flash.bloop.client.services;

import com.microservices.flash.bloop.common.data.dtos.MessageDto;

public interface WordCensorshipService {

    MessageDto censorWords(MessageDto message);
    
}
