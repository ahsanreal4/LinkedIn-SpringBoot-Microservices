package com.linkedIn.users_service.service.impl;

import com.linkedIn.users_service.dto.UpdateUserDto;
import com.linkedIn.users_service.dto.UserDto;
import com.linkedIn.users_service.dto.UserFileDto;
import com.linkedIn.users_service.dto.UserProfileDto;
import com.linkedIn.users_service.dto.resume.UserResumeDto;
import com.linkedIn.users_service.entity.User;
import com.linkedIn.users_service.entity.UserFiles;
import com.linkedIn.users_service.entity.UserInfo;
import com.linkedIn.users_service.enums.UserFileType;
import com.linkedIn.users_service.exception.ApiException;
import com.linkedIn.users_service.feign_clients.impl.FileServiceClientImpl;
import com.linkedIn.users_service.repository.user.UserFileRepository;
import com.linkedIn.users_service.repository.user.UserRepository;
import com.linkedIn.users_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final String USER_NAME_ALREADY_EXISTS = "username already exists";
    private final String USER_DOES_NOT_EXIST = "user does not exist";

    private final UserRepository userRepository;
    private final UserFileRepository userFileRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final FileServiceClientImpl fileService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder, UserFileRepository userFileRepository,
                           FileServiceClientImpl fileService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userFileRepository = userFileRepository;
        this.fileService = fileService;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = this.userRepository.findAll();

        return users.stream().map(curr -> modelMapper.map(curr, UserDto.class)).toList();
    }

    @Override
    public String deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, USER_DOES_NOT_EXIST)
        );

        userRepository.delete(user);
        return "User deleted successfully";
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUserDto, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        if (updateUserDto.getUsername() != null && !updateUserDto.getUsername().equals(user.getUsername())) {
            String username = updateUserDto.getUsername();

            if(userRepository.findByUsername(username).isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, USER_NAME_ALREADY_EXISTS);

            user.setUsername(username);
        }

        if (updateUserDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updateUserDto.getPassword());
            user.setPassword(encodedPassword);
        }
        if (updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserProfileDto getProfile(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        return mapUserToProfileDto(user);
    }

    @Override
    public UserProfileDto getProfileById(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(optionalUser.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "user with id " + userId + " was not found");

        User user = optionalUser.get();

        return mapUserToProfileDto(user);
    }

    @Override
    public List<UserDto> getUserFriends(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        Set<User> friends = user.getFriends();

        if (friends == null) return new ArrayList<>();

        List<User> friendsList = friends.stream().toList();

        return friendsList.stream().map(friend -> modelMapper.map(friend, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public String uploadUserFile(MultipartFile file, String fileType, long userId) {
        UserFileType userFileType;

        try {
            userFileType = UserFileType.valueOf(fileType.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid File Type. Only " + UserFileType.LOGO + ", " + UserFileType.BANNER + ", " + UserFileType.RESUME + " are supported");
        }

        String url;

        try {
            url = fileService.uploadFile(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading file");
        }


        if (!userFileType.equals(UserFileType.RESUME)) {
            Optional<UserFiles> optionalUserFiles = userFileRepository.findByUserIdAndType(userId, userFileType);

            // If already present just update the link
            // If file type resume then allow to upload more files
            if (optionalUserFiles.isPresent()) {
                UserFiles userFile = optionalUserFiles.get();
                userFile.setLink(url);

                userFileRepository.save(userFile);

                return url;
            }
        }

        User user = new User();
        user.setId(userId);

        UserFiles userFiles = new UserFiles();
        userFiles.setType(userFileType);
        userFiles.setLink(url);
        userFiles.setUser(user);
        userFiles.setName(file.getOriginalFilename());

        userFileRepository.save(userFiles);

        return url;
    }

    @Override
    public List<UserFileDto> getAllFiles(long userId) {
        List<UserFiles> userFiles = userFileRepository.findByUserId(userId);

        return userFiles.stream().map(userFile -> {
            UserFileDto userFileDto = new UserFileDto();
            userFileDto.setType(userFile.getType().toString());
            userFileDto.setLink(userFile.getLink());

            return userFileDto;
        }).collect(Collectors.toList());
    }

    @Override
    public UserFileDto getFileByType(String fileType, long userId) {
        UserFileType userFileType;

        try {
            userFileType = UserFileType.valueOf(fileType.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid File Type. Only " + UserFileType.LOGO + ", " + UserFileType.BANNER + ", " + UserFileType.RESUME + " are supported");
        }

        Optional<UserFiles> optionalUserFile = userFileRepository.findByUserIdAndType(userId, userFileType);

        if (optionalUserFile.isEmpty()) throw new ApiException(HttpStatus.NOT_FOUND, "user file was not found");

        UserFiles userFile = optionalUserFile.get();

        UserFileDto userFileDto = new UserFileDto();
        userFileDto.setType(userFile.getType().toString());
        userFileDto.setLink(userFile.getLink());

        return userFileDto;
    }

    @Override
    public List<UserResumeDto> getUserResumes(long userId) {
        List<UserFiles> userFiles = userFileRepository.findByUserId(userId);

        List<UserFiles> filteredFiles = userFiles.stream().filter(userFile -> userFile.getType().equals(UserFileType.RESUME)).toList();

        return filteredFiles.stream().map(userFile -> {
            UserResumeDto userResumeDto = new UserResumeDto();
            userResumeDto.setId(userFile.getId());
            userResumeDto.setName(userFile.getName());
            userResumeDto.setLink(userFile.getLink());

            return userResumeDto;
        }).collect(Collectors.toList());
    }

    private UserProfileDto mapUserToProfileDto(User user) {
        UserProfileDto userProfileDto = modelMapper.map(user, UserProfileDto.class);
        UserInfo userInfo = user.getUserInfo();
        if(userInfo != null) modelMapper.map(userInfo, userProfileDto);

        Optional<UserFiles> optionalLogo = userFileRepository.findByUserIdAndType(user.getId(), UserFileType.LOGO);
        Optional<UserFiles> optionalBanner = userFileRepository.findByUserIdAndType(user.getId(), UserFileType.BANNER);

        optionalLogo.ifPresent(userFiles -> userProfileDto.setLogo(userFiles.getLink()));
        optionalBanner.ifPresent(userFiles -> userProfileDto.setBanner(userFiles.getLink()));

        return userProfileDto;
    }
}
