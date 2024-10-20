package com.linkedIn.users_service.repository.user;

import com.linkedIn.users_service.entity.UserFiles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFiles, Long> {
}
