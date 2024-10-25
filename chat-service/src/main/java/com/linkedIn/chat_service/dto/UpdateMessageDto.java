package com.linkedIn.chat_service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateMessageDto {
    @NotEmpty
    private String text;
}
