package com.linkedIn.users_service.repository.user;

import com.linkedIn.users_service.entity.UserFiles;
import com.linkedIn.users_service.enums.UserFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFileRepository extends JpaRepository<UserFiles, Long> {
    Optional<UserFiles> findByUserIdAndType(long userId, UserFileType type);

    List<UserFiles> findByUserId(long userId);

}
