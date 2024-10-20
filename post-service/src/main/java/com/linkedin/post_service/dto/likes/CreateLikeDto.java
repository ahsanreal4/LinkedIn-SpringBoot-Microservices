package com.linkedin.post_service.dto.likes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateLikeDto {

    @NotNull
    @Positive
    private long postId;
}
