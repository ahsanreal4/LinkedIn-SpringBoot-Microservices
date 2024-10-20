package com.linkedIn.users_service.service;

import com.linkedIn.users_service.dto.UpdateUserDto;
import com.linkedIn.users_service.dto.UserDto;
import com.linkedIn.users_service.dto.UserProfileDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    String deleteUser(long id);

    UserDto updateUser(UpdateUserDto updateUserDto, String email);

    UserProfileDto getProfile(String email);

    List<UserDto> getUserFriends(String email);
}
