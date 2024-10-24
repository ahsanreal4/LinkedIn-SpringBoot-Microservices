package com.linkedIn.company_job_service.feign_clients.impl;

import com.linkedIn.company_job_service.dto.job.UserProfileDto;
import com.linkedIn.company_job_service.feign_clients.UserServiceClent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceClientImpl {

    @Autowired
    private UserServiceClent userServiceClent;

    public UserProfileDto getUserProfile(long userId) { return this.userServiceClent.getUserProfile(userId); }
}
