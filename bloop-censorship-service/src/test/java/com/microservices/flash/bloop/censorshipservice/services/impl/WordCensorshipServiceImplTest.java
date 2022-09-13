package com.microservices.flash.bloop.censorshipservice.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.microservices.flash.bloop.censorshipservice.domain.SensitiveWord;
import com.microservices.flash.bloop.censorshipservice.repositories.SensitiveWordRepository;
import com.microservices.flash.bloop.censorshipservice.services.WordCensorshipService;
import com.microservices.flash.bloop.censorshipservice.utils.SensitiveWordsList;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;

/**
 * This should tell us whether our mapping is correct or not
 * 
 * The Web MVC test is very limited in that it does not bring in the Service Layer
 *     Therefore, we implement a MockBean using mockito
 */
@WebMvcTest(WordCensorshipServiceImpl.class)
public class WordCensorshipServiceImplTest {

    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    WordCensorshipService wordCensorshipService;

    /**
     * Set the property for the url automatically and get everything configured
     */
    @MockBean
    SensitiveWordRepository sensitiveWordRepository;

    List<SensitiveWord> mockSensitiveWords;

    /**
     * BeforeEach - Execute the method before each test method 
     */
    @BeforeEach
    void setUp() {
        mockSensitiveWords = SensitiveWordsList.sensitiveWords
                                .stream()             
                                .map(word -> SensitiveWord.builder().text(word).build())
                                .collect(Collectors.toList());

        wordCensorshipService = new WordCensorshipServiceImpl(sensitiveWordRepository);
    }


    @Test
    void testCensorSensitiveWords() {
        Mockito.when(sensitiveWordRepository.findAll()).thenReturn(mockSensitiveWords);

        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("TAFADZWA CURRENT_SCHEMAIDX-SEQUENCE7")
                                .build();

        MessageDto censoredMessage = wordCensorshipService.censorSensitiveWords(message);

        assertThat(censoredMessage.getText()).isEqualTo("TAFADZWA ****************X-********7");

    }

    @Test
    void testUpperLowerCensorSensitiveWords() {
        Mockito.when(sensitiveWordRepository.findAll()).thenReturn(mockSensitiveWords);

        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("TAFADZWA CURREnt_SCHEMAIDX-SEQUENCE7")
                                .build();

        MessageDto censoredMessage = wordCensorshipService.censorSensitiveWords(message);

        assertThat(censoredMessage.getText()).isEqualTo("TAFADZWA ****************X-********7");

    }

    @Test
    void testCensorSensitiveWordsHappyPath() {
        Mockito.when(sensitiveWordRepository.findAll()).thenReturn(mockSensitiveWords);

        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("TAFADZWA MANDAVA")
                                .build();

        MessageDto censoredMessage = wordCensorshipService.censorSensitiveWords(message);

        assertThat(censoredMessage.getText()).isEqualTo("TAFADZWA MANDAVA");

    }


    @Test
    void testEscapeSpecialCharCensorSensitiveWords() {
        Mockito.when(sensitiveWordRepository.findAll()).thenReturn(mockSensitiveWords);

        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("SELECT * FROM")
                                .build();

        MessageDto censoredMessage = wordCensorshipService.censorSensitiveWords(message);

        assertThat(censoredMessage.getText()).isEqualTo("*************");   
        
    }

    @Test
    void testUpperLowerEscapeSpecialCharCensorSensitiveWords() {
        Mockito.when(sensitiveWordRepository.findAll()).thenReturn(mockSensitiveWords);

        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("SELECT * from")
                                .build();

        MessageDto censoredMessage = wordCensorshipService.censorSensitiveWords(message);

        assertThat(censoredMessage.getText()).isEqualTo("*************");   
        
    }    

    @Test
    void testExtendedEscapeSpecialCharCensorSensitiveWords() {
        Mockito.when(sensitiveWordRepository.findAll()).thenReturn(mockSensitiveWords);

        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("TAFADZWASELECT * FROM MANDAVA")
                                .build();

        MessageDto censoredMessage = wordCensorshipService.censorSensitiveWords(message);

        assertThat(censoredMessage.getText()).isEqualTo("TAFADZWA************* MANDAVA");   
        
    }


}
