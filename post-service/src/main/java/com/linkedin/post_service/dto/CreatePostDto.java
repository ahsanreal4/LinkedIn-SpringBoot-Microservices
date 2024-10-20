package com.linkedin.post_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostDto {
    @NotEmpty
    @Size(min =  5, max = 100)
    private String title;

    @NotEmpty
    @Size(min =  10)
    private String description;
}
