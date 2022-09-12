package com.microservices.flash.bloop.client.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.client.repositories.MessageRepository;
import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.client.services.MessageService;
import com.microservices.flash.bloop.client.services.WordCensorshipService;
import com.microservices.flash.bloop.client.utils.MemberAccountUtil;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.entities.Message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    public static final int MESSAGES_PER_PAGE = 10;

    private final MessageRepository messageRepository;

    private final MemberService memberService;

    private final WordCensorshipService wordCensorshipService;

    @Override
    public List<Message> listAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    @Override
    public Page<Message> listByPage(HttpServletRequest request, int pageNumber, String sortField, String sortDirection) {

        Member member = memberService.findByEmail(MemberAccountUtil.getAuthenticatedMemberEmail(request));

        Sort sort = Sort.by(sortField);

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        /**
         * Page number is zero base 
         *    i.e the first page corresponds to page number zero 
         *        However, we will be passing page numbers starting from to number one. Therefor, we need to subtract fone from the value of pageNumber being passed
         */
        Pageable pageable = PageRequest.of(pageNumber - 1, MESSAGES_PER_PAGE, sort);


        return messageRepository.findByMemberAndIsDeletedEquals(member, false, pageable);

    }

    @Override
    public Message saveMessage(HttpServletRequest request, Message formMessage) {
        boolean isUpdateMode = !ObjectUtils.isEmpty(formMessage.getId());

        if (isUpdateMode) {
            Message dbMessage = messageRepository.getReferenceById(formMessage.getId());
            
            // Invoke WordCensorshipService
            dbMessage.setText(wordCensorshipService.censorWords(formMessage).getText());
            return messageRepository.save(dbMessage);

        } else {
            Member member = memberService.findByEmail(MemberAccountUtil.getAuthenticatedMemberEmail(request));
            
            // Invoke WordCensorshipService
            return messageRepository.save(formMessage.builder().text(wordCensorshipService.censorWords(formMessage).getText()).member(member).build());
        }
    }

    @Override
    public Message updateMessage(Message formMessage) {
        Message dbMessage = messageRepository.getReferenceById(formMessage.getId());

        // Invoke WordCensorshipService
        dbMessage.setText(wordCensorshipService.censorWords(formMessage).getText());
        return messageRepository.save(dbMessage);
    }

    @Override
    public Message findById(UUID id) throws MessageNotFoundException {
        try {

            return messageRepository.findById(id).get();

        } catch (NoSuchElementException e) {

            throw new MessageNotFoundException(String.format("Could not find any message with ID '%s'", id));

        } 
    }

    @Override
    public void deleteMessage(UUID id) throws MessageNotFoundException {

        try {
            Message dbMessage = messageRepository.getReferenceById(id);

            dbMessage.setDeleted(true);

            messageRepository.save(dbMessage);

        } catch (NoSuchElementException e) {

            throw new MessageNotFoundException(String.format("Could not find any message with ID '%s'", id));

        } 

    }
    
}
