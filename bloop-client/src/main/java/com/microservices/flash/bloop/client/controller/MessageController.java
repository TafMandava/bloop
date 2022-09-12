package com.microservices.flash.bloop.client.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.client.services.MessageService;
import com.microservices.flash.bloop.client.services.impl.MessageServiceImpl;
import com.microservices.flash.bloop.common.data.entities.Message;
import com.microservices.flash.bloop.common.data.mappers.MessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/messages")
public class MessageController {
 
    private final MessageService messageService;

    private final MessageMapper messageMapper;

    @GetMapping
    public String listFirstPage(HttpServletRequest request, Model model) {
        
        return listByPage(request, 1, model, "text", "asc");
    }
    
    @GetMapping("/page/{pageNumber}")
    public String listByPage(HttpServletRequest request,
                             @PathVariable(name = "pageNumber") int pageNumber, 
                             Model model, 
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection) {

        log.debug("Sort field '{}'", sortField);
        log.debug("Sort direction '{}'", sortDirection);

        Page<Message> page = messageService.listByPage(request, pageNumber, sortField, sortDirection);
        List<Message> messages = page.getContent();

        log.debug("Page number '{}'", pageNumber);
        log.debug("Total elements '{}'", page.getTotalElements());
        log.debug("Total pages '{}'", page.getTotalPages());

        long startCount = (pageNumber - 1) * MessageServiceImpl.MESSAGES_PER_PAGE + 1;
        long endCount = startCount + MessageServiceImpl.MESSAGES_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalElements", page.getTotalElements());

        model.addAttribute("textMessages", messages);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);

        return "messages/messages";

    } 
    
    @GetMapping("/create")
    public String createMessage(HttpServletRequest request, Model model) {
        

        Message message = Message.builder().isDeleted(false).build();
        model.addAttribute("textMessage", message);
        model.addAttribute("pageTitle", "Create Message");

        return "messages/message-form";
    } 
    

    /**
     * Displaying successful message
     *     We need access to redirect attributes
     * @throws IOException
     */
    @PostMapping("/save")
    public String saveUser(HttpServletRequest request, Message message, RedirectAttributes redirectAttributes) throws IOException {


        messageService.saveMessage(request, message);

        redirectAttributes.addFlashAttribute("message", "The message has been saved successfully.");
        
        return "redirect:/messages";
    }    

    
    @GetMapping("/edit/{id}")
    public String editMessage(@PathVariable("id") String id, RedirectAttributes redirectAttributes, Model model) {

        try {
            Message message = messageService.findById(UUID.fromString(id));

            model.addAttribute("textMessage", message);
            model.addAttribute("pageTitle", "Edit Text");

            return "messages/message-form";

        }  catch(MessageNotFoundException e) {

            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/messages";

        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") String id, RedirectAttributes redirectAttributes, Model model) {

        try {

            messageService.deleteMessage(UUID.fromString(id));

            redirectAttributes.addFlashAttribute("message", "The message was deleted successfully");

        }  catch(MessageNotFoundException e) {

            redirectAttributes.addFlashAttribute("message", e.getMessage());

        }

        return "redirect:/messages";        

    }    


}
