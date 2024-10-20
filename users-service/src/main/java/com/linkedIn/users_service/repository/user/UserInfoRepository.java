package com.linkedIn.users_service.repository.user;

import com.linkedIn.users_service.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
