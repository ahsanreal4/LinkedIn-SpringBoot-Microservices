package com.linkedIn.users_service.controller;

import com.linkedIn.users_service.dto.UpdateUserDto;
import com.linkedIn.users_service.dto.UserDto;
import com.linkedIn.users_service.dto.UserProfileDto;
import com.linkedIn.users_service.service.UserService;
import com.linkedIn.users_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserUtils userUtils;

    public UserController(UserService userService, UserUtils userUtils) {
        super();
        this.userService = userService;
        this.userUtils = userUtils;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers(HttpServletRequest request) {
        userUtils.shouldBeAdmin(request);

        List<UserDto> users = userService.getUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long id,
                                             HttpServletRequest request) {
        userUtils.shouldBeAdmin(request);

        String message = userService.deleteUser(id);

        return ResponseEntity.ok(message);
    }

    @PutMapping("")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto, HttpServletRequest request) {
        String email = userUtils.getUserEmail(request);

        UserDto userDto = this.userService.updateUser(updateUserDto, email);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(HttpServletRequest request) {
        String email = userUtils.getUserEmail(request);

        UserProfileDto userProfileDto = this.userService.getProfile(email);

        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserDto>> getUserFriends(HttpServletRequest request) {
        String email = userUtils.getUserEmail(request);

        List<UserDto> friends = this.userService.getUserFriends(email);

        return ResponseEntity.ok(friends);
    }
}
