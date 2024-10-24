package com.linkedIn.company_job_service.feign_clients;

import com.linkedIn.company_job_service.dto.job.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="users-service", url="http://localhost:8081")
public interface UserServiceClent {

    @GetMapping("/api/users/profile/{id}")
    UserProfileDto getUserProfile(@PathVariable("id") long id);

}
