package com.microservices.flash.bloop.censorshipservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.microservices.flash.bloop.common.data.dtos.MessageDto;

/**
 * This should tell us whether our mapping is correct or not
 * 
 * The Web MVC test is very limited in that it does not bring in the Service Layer
 *     Therefore, we will use SpringBootTest for a lighly more realistic integration test. We do not wan to use mock beans
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SensitiveWordControllerTest {

    public static final String CENSOR_PATH_V1 = "/api/v1/censor";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void testCensorWords() {
        MessageDto message = MessageDto.builder()
                                .id(UUID.randomUUID())
                                .text("TAFADZWA CURREnt_SCHEMAIDX-SEQUENCE7")
                                .build();
                            
        ResponseEntity<MessageDto> response = postMessage(message, MessageDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody().getText()).isEqualTo("TAFADZWA ****************X-********7");
    }

    private <T> ResponseEntity<T> postMessage(MessageDto message, Class<T> responseType) {
        return testRestTemplate.postForEntity(CENSOR_PATH_V1, message, responseType);
    }

}
