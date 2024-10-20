package com.linkedin.post_service.repository;

import com.linkedin.post_service.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentsRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostIdAndParentId(long postId, Long parentId);
    int countByPostId(long postId);
    List<PostComment> findByParentId(long id);
}
