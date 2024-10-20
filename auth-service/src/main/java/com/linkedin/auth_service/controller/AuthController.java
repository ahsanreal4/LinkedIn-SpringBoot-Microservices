package com.linkedin.auth_service.controller;

import com.linkedin.auth_service.dto.LoginUserDto;
import com.linkedin.auth_service.dto.RegisterUserDto;
import com.linkedin.auth_service.dto.UserDto;
import com.linkedin.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService)  {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto userDto = this.authService.createUser(registerUserDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        String token = this.authService.login(loginUserDto);

        return ResponseEntity.ok(token);
    }
}
