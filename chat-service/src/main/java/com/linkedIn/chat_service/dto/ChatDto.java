package com.linkedIn.chat_service.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ChatDto {
    private long id;
    private UserProfileDto sender;
    private UserProfileDto receiver;
    private Date createdAt;
    private List<MessageDto> messages;
}
