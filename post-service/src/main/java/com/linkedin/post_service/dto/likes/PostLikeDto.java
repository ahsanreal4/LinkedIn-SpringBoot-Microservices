package com.linkedin.post_service.dto.likes;

import lombok.Data;

@Data
public class PostLikeDto {
    private Long id;
    private Long userId;
    private Long postId;
}
