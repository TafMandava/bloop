package com.microservices.flash.bloop.client.services;

import com.microservices.flash.bloop.common.data.entities.Message;

public interface WordCensorshipService {

    Message censorWords(Message message);
    
}
