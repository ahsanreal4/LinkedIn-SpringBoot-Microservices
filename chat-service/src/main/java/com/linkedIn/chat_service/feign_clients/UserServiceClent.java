package com.linkedIn.chat_service.feign_clients;

import com.linkedIn.chat_service.dto.UserDto;
import com.linkedIn.chat_service.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="users-service", url="http://localhost:8081")
public interface UserServiceClent {
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") long id);

    @GetMapping("/api/users/profile/{id}")
    UserProfileDto getUserProfile(@PathVariable("id") long id);

}
