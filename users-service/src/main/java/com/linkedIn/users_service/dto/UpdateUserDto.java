package com.linkedIn.users_service.dto;

import com.linkedIn.users_service.validators.atLeastOneNotNull.AtLeastOneNotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AtLeastOneNotNull
public class UpdateUserDto {
    @Size(min = 5, max = 50)
    private String username;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    @Size(min = 8, max = 12)
    private String password;
}