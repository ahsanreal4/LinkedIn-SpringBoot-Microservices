package com.linkedin.post_service.repository;

import com.linkedin.post_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
