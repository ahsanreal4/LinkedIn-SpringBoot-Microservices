package com.linkedin.auth_service.service;

import com.linkedin.auth_service.dto.LoginUserDto;
import com.linkedin.auth_service.dto.RegisterUserDto;
import com.linkedin.auth_service.dto.UserDto;

public interface AuthService {
    UserDto createUser(RegisterUserDto registerUserDto);

    String login(LoginUserDto loginUserDto);
}
