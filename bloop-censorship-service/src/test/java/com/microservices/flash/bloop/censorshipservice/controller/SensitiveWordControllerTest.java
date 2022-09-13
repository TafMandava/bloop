package com.microservices.flash.bloop.censorshipservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.microservices.flash.bloop.common.data.entities.Message;

/**
 * This should tell us whether our mapping is correct or not
 * 
 * The Web MVC test is very limited in that it does not bring in the Service Layer
 *     Therefore, we will use SpringBootTest for a lighly more realistic integration test. We do not wan to use mock beans
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SensitiveWordControllerTest {

    public static final String MESSAGE_PATH_V1 = "/api/v1/message";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void testCensorWords() {
        Message message = Message.builder()
                            .id(UUID.randomUUID())
                            .text("TAFADZWA CURREnt_SCHEMAIDX-SEQUENCE7")
                            .build();
                            
        ResponseEntity<Message> response = postMessage(message, Message.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody().getText()).isEqualTo("TAFADZWA ****************X-********7");
    }

    private <T> ResponseEntity<T> postMessage(Message message, Class<T> responseType) {
        return testRestTemplate.postForEntity(MESSAGE_PATH_V1, message, responseType);
    }

}
