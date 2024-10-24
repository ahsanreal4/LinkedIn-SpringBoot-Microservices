package com.linkedIn.users_service.service;

import com.linkedIn.users_service.dto.UpdateUserDto;
import com.linkedIn.users_service.dto.UserDto;
import com.linkedIn.users_service.dto.UserFileDto;
import com.linkedIn.users_service.dto.UserProfileDto;
import com.linkedIn.users_service.dto.resume.UserResumeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    String deleteUser(long id);

    UserDto updateUser(UpdateUserDto updateUserDto, String email);

    UserProfileDto getProfile(String email);

    UserProfileDto getProfileById(long userId);

    List<UserDto> getUserFriends(String email);

    String uploadUserFile(MultipartFile file, String fileType, long userId);

    List<UserFileDto> getAllFiles(long userId);

    UserFileDto getFileByType(String fileType, long userId);

    List<UserResumeDto> getUserResumes(long userId);
}
