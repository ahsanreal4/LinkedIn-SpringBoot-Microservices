package com.linkedin.post_service.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostCommentDto {
    @Positive
    @NotNull
    private Long postId;

    @Positive
    private Long parentId;

    @NotEmpty
    @Size(min = 1, max = 255)
    private String text;
}
