package com.linkedin.auth_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Data
@Entity
@Table
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column
    private String address;

    @Column(length = 20)
    private String zipCode;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false,length = 50)
    private String country;

    @Column
    private String website;

    @Column(columnDefinition = "TEXT")
    private String professionalSummary;

    @Column()
    private String headLine;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dob;

}
