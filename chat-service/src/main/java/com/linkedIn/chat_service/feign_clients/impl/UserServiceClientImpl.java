package com.linkedIn.chat_service.feign_clients.impl;

import com.linkedIn.chat_service.dto.UserDto;
import com.linkedIn.chat_service.dto.UserProfileDto;
import com.linkedIn.chat_service.feign_clients.UserServiceClent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceClientImpl {

    @Autowired
    private UserServiceClent userServiceClent;

    public UserDto getUserById(long userId) { return this.userServiceClent.getUserById(userId); }

    public UserProfileDto getUserProfileById(long userId) { return this.userServiceClent.getUserProfile(userId); }

}
