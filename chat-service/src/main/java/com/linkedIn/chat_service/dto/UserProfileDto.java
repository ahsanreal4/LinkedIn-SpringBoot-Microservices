package com.linkedIn.chat_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String zipCode;
    private String city;
    private String country;
    private String website;
    private String professionalSummary;
    private String headLine;
    private LocalDate dob;
    private String logo;
    private String banner;
}
