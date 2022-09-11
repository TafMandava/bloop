package com.microservices.flash.bloop.client.services;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.common.data.entities.Message;

public interface MessageService {

    public List<Message> listAllMessages();

    public Page<Message> listByPage(HttpServletRequest request, int pageNumber, String sortField, String sortDirection);
    
    public Message saveMessage(HttpServletRequest request, Message formMessage);

    public Message updateMessage(Message formMessage);

    public Message findById(UUID id) throws MessageNotFoundException;

    public void deleteMessage(UUID id) throws MessageNotFoundException;
}
