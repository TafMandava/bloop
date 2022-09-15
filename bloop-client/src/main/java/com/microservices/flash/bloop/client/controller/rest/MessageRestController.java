package com.microservices.flash.bloop.client.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.client.services.MessageService;
import com.microservices.flash.bloop.client.services.impl.MessageServiceImpl;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/message")
public class MessageRestController {


    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> listFirstPage(HttpServletRequest request) {
        
        return listByPage(request, 1, "text", "asc");
    }
    
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<List<MessageDto>> listByPage(HttpServletRequest request,
                             @PathVariable(name = "pageNumber") int pageNumber, 
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection) {

        log.debug("Sort field '{}'", sortField);
        log.debug("Sort direction '{}'", sortDirection);

        Page<MessageDto> page = messageService.listByPage(request, pageNumber, sortField, sortDirection);
        List<MessageDto> messages = page.getContent();

        log.debug("Page number '{}'", pageNumber);
        log.debug("Total elements '{}'", page.getTotalElements());
        log.debug("Total pages '{}'", page.getTotalPages());

        long startCount = (pageNumber - 1) * MessageServiceImpl.MESSAGES_PER_PAGE + 1;
        long endCount = startCount + MessageServiceImpl.MESSAGES_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        return new ResponseEntity<>(messages, HttpStatus.OK);

    }
    
    /**
     * Displaying successful message
     *     We need access to redirect attributes
     * @throws IOException
     */
    @PostMapping("/save")
    public ResponseEntity<MessageDto> saveUser(HttpServletRequest request, MessageDto message) throws IOException {


        messageService.saveMessage(request, message);

        
        return new ResponseEntity<>(messageService.saveMessage(request, message), HttpStatus.CREATED);
    }    

    
    @PutMapping("/edit/{id}")
    public ResponseEntity<MessageDto> editMessage(@PathVariable("id") String id) throws MessageNotFoundException {


        MessageDto message = messageService.findById(UUID.fromString(id));

        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("id") String id) throws MessageNotFoundException {

        messageService.deleteMessage(UUID.fromString(id));
       
    }        

}
