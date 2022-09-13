package com.microservices.flash.bloop.client.model;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.microservices.flash.bloop.common.data.dtos.MessageDto;

public class MessagePagedList extends PageImpl<MessageDto> {

    /**
     * Added this constructor 
     *     pageable, last, totalPages, sort, first and numberOfElements are not referenced by the constructor because we are going to create another POJO
     * 
     * We are giving Jackson the capeability to talk to another Spring Framework service where it has a paged property and then binds to a Message Paged List 
     * So this is going to handle the Json bindings of those paged objects do get complicated and the primary focus is how the Json creator can be made
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MessagePagedList(@JsonProperty("content") List<MessageDto> content,
                         @JsonProperty("number") int number,
                         @JsonProperty("size") int size,
                         @JsonProperty("totalElements") Long totalElements,
                         @JsonProperty("pageable") JsonNode pageable,
                         @JsonProperty("last") boolean last,
                         @JsonProperty("totalPages") int totalPages,
                         @JsonProperty("sort") JsonNode sort,
                         @JsonProperty("first") boolean first,
                         @JsonProperty("numberOfElements") int numberOfElements) {
        
        super(content, PageRequest.of(number, size), totalElements);

    }

    public MessagePagedList(List<MessageDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MessagePagedList(List<MessageDto> content) {
        super(content);
    }
    
}
