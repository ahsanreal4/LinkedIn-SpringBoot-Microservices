package com.linkedin.post_service.repository;

import com.linkedin.post_service.entity.PostFiles;
import com.linkedin.post_service.enums.PostFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostFilesRepository extends JpaRepository<PostFiles, Long> {
    Optional<PostFiles> findByPostId(long postId);
}
