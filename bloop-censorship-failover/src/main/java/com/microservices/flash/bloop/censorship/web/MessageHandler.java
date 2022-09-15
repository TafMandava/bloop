package com.microservices.flash.bloop.censorship.web;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.microservices.flash.bloop.common.data.dtos.MessageDto;

import reactor.core.publisher.Mono;

@Component
public class MessageHandler {

    public Mono<ServerResponse> getMessage(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(
                        MessageDto.builder()
                            .id(UUID.randomUUID())
                            .text("Bloop")
                            .createdDate(OffsetDateTime.now())
                            .lastModifiedDate(OffsetDateTime.now())
                            .build()), MessageDto.class);
    }    
    
}
