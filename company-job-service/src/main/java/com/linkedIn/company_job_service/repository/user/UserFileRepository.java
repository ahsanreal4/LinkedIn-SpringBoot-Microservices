package com.linkedIn.company_job_service.repository.user;

import com.linkedIn.company_job_service.entity.user.UserFiles;
import com.linkedIn.company_job_service.enums.UserFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFiles, Long> {
    Optional<UserFiles> findByUserIdAndType(long userId, UserFileType type);

    List<UserFiles> findByUserId(long userId);

}
