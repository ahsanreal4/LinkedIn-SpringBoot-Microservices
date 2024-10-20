package com.linkedin.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDto {
    @NotEmpty()
    @Email
    private String email;

    @NotEmpty
    @Size(min = 8, max = 12)
    private String password;
}
