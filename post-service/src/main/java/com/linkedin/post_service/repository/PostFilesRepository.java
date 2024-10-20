package com.linkedin.post_service.repository;

import com.linkedin.post_service.entity.PostFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFilesRepository extends JpaRepository<PostFiles, Long> {
}
