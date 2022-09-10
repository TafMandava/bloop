package com.microservices.flash.bloop.common.data.mappers;

import org.mapstruct.Mapper;

import com.microservices.flash.bloop.common.data.dtos.MessageDto;
import com.microservices.flash.bloop.common.data.entities.Message;

@Mapper(uses = {DateMapper.class})
public interface MessageMapper {

    MessageDto messageToMessageDto(Message message);
    Message messageDtoToMessage(MessageDto messageDto);
    
}
