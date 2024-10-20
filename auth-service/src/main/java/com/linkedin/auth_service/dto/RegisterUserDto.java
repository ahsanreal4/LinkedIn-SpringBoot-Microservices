package com.linkedin.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    @NotEmpty
    @Size(min = 5, max = 50)
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String lastName;

    @NotEmpty
    @Size(min = 8, max = 12)
    private String password;
}
