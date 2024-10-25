package com.linkedIn.chat_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateChatDto {
    @NotNull
    @Positive
    private Long receiverId;
}
