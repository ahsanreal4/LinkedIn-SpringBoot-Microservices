package com.linkedIn.chat_service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDto {
    private long id;
    private String text;
    private Date sentAt;
}
