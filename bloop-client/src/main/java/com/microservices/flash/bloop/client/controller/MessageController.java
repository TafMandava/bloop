package com.microservices.flash.bloop.client.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microservices.flash.bloop.client.exceptions.MessageNotFoundException;
import com.microservices.flash.bloop.client.security.MemberUserDetails;
import com.microservices.flash.bloop.client.services.MemberService;
import com.microservices.flash.bloop.client.services.MessageService;
import com.microservices.flash.bloop.client.services.impl.MessageServiceImpl;
import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.entities.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/messages")
public class MessageController {
 
    private final MessageService messageService;

    private final MemberService memberService;

    @GetMapping
    public String listFirstPage(@AuthenticationPrincipal MemberUserDetails loggedMember, Model model) {
        
        return listByPage(loggedMember, 1, model, "text", "asc");
    }
    
    @GetMapping("/page/{pageNumber}")
    public String listByPage(@AuthenticationPrincipal MemberUserDetails loggedMember,
                             @PathVariable(name = "pageNumber") int pageNumber, 
                             Model model, 
                             @Param("sortField") String sortField,
                             @Param("sortDirection") String sortDirection) {

        log.debug("Sort field '{}'", sortField);
        log.debug("Sort direction '{}'", sortDirection);

        Page<Message> page = messageService.listByPage(loggedMember, pageNumber, sortField, sortDirection);
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

        model.addAttribute("messages", messages);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", reverseSortDirection);

        return "messages/messages";

    } 
    
    @GetMapping("/create")
    public String createMessage(@AuthenticationPrincipal MemberUserDetails loggedMember, Model model) {
        
        String email = loggedMember.getUsername();
        Member member = memberService.findByEmail(email);

        Message message = Message.builder().isDeleted(false).member(member).build();
        model.addAttribute("message", message);
        model.addAttribute("pageTitle", "Create Message");

        return "messages/message-form";
    } 
    
    
    @GetMapping("/edit/{id}")
    public String editMessage(@PathVariable("id") String id, RedirectAttributes redirectAttributes, Model model) {

        try {
            Message message = messageService.findById(UUID.fromString(id));

            model.addAttribute("message", message);
            model.addAttribute("pageTitle", "Edit Message");

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
