package com.microservices.flash.bloop.client.services;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;

public interface MessageService {

    public List<MessageDto> listAllMessages();

    public Page<MessageDto> listByPage(HttpServletRequest request, int pageNumber, String sortField, String sortDirection);
    
    public MessageDto saveMessage(HttpServletRequest request, MessageDto formMessage);

    public MessageDto updateMessage(MessageDto formMessage);

    public MessageDto findById(UUID id) throws MessageNotFoundException;

    public void deleteMessage(UUID id) throws MessageNotFoundException;
}
