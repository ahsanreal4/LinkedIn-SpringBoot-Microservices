package com.linkedIn.chat_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateMessageDto {
    @NotNull
    @Positive
    private Long chatId;

    @NotEmpty
    private String text;
}
