package com.microservices.flash.bloop.client.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.client.model.MessagePagedList;
import com.microservices.flash.bloop.client.repositories.MessageRepository;
import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.client.services.MessageService;
import com.microservices.flash.bloop.client.services.WordCensorshipService;
import com.microservices.flash.bloop.client.utils.MemberAccountUtil;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.entities.Message;
import com.microservices.flash.bloop.common.data.mappers.MessageMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    public static final int MESSAGES_PER_PAGE = 10;

    private final MessageRepository messageRepository;

    private final MemberService memberService;

    private final WordCensorshipService wordCensorshipService;

    private final MessageMapper messageMapper;

    @Override
    public List<MessageDto> listAllMessages() {

        return messageRepository.findAll()
                .stream()
                .map(messageMapper::messageToMessageDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessagePagedList listByPage(HttpServletRequest request, int pageNumber, String sortField, String sortDirection) {

        Member member = memberService.findByEmail(MemberAccountUtil.getAuthenticatedMemberEmail(request));

        Sort sort = Sort.by(sortField);

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        /**
         * Page number is zero base 
         *    i.e the first page corresponds to page number zero 
         *        However, we will be passing page numbers starting from to number one. Therefor, we need to subtract fone from the value of pageNumber being passed
         */
        Pageable pageable = PageRequest.of(pageNumber - 1, MESSAGES_PER_PAGE, sort);


        Page<Message> messagePage = messageRepository.findByMemberAndIsDeletedEquals(member, false, pageable);

        return new MessagePagedList(messagePage.getContent()
                        .stream()
                        .map(messageMapper::messageToMessageDto)
                        .collect(Collectors.toList()),
                            PageRequest.of(messagePage.getPageable().getPageNumber(), messagePage.getPageable().getPageSize()),
                         messagePage.getTotalElements()
                    );

    }

    @Override
    public MessageDto saveMessage(HttpServletRequest request, MessageDto formMessage) {
        boolean isUpdateMode = !ObjectUtils.isEmpty(formMessage.getId());

        if (isUpdateMode) {
            Message dbMessage = messageRepository.getReferenceById(formMessage.getId());
            
            // Invoke WordCensorshipService
            dbMessage.setText(wordCensorshipService.censorWords(formMessage).getText());

            return messageMapper.messageToMessageDto( messageRepository.save(dbMessage) );

        } else {
            Member member = memberService.findByEmail(MemberAccountUtil.getAuthenticatedMemberEmail(request));

            Message message = messageMapper.messageDtoToMessage(formMessage);
            
            // Invoke WordCensorshipService
            return  messageMapper.messageToMessageDto( 
                                    messageRepository.save(  
                                        message.builder()
                                                .text(
                                                    wordCensorshipService.censorWords(formMessage).getText()
                                                )
                                                .isDeleted(false)
                                                .member(member)
                                                .build() 
                                    ) 
                                );
        }
    }

    @Override
    public MessageDto updateMessage(MessageDto formMessage) {
        Message dbMessage = messageRepository.getReferenceById(formMessage.getId());

        // Invoke WordCensorshipService
        dbMessage.setText(wordCensorshipService.censorWords(formMessage).getText());

        return messageMapper.messageToMessageDto( 
                    messageRepository.save(dbMessage) 
                );
    }

    @Override
    public MessageDto findById(UUID id) throws MessageNotFoundException {
        try {

            return messageMapper.messageToMessageDto( 
                        messageRepository.findById(id)
                                         .get() 
                    );

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
