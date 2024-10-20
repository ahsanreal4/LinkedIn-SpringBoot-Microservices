package com.linkedIn.users_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserFriendsDto {
    List<UserDto> userFriends;
}
