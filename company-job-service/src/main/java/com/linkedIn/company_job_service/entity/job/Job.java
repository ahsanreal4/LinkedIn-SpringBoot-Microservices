package com.linkedIn.company_job_service.entity.job;

import com.linkedIn.company_job_service.entity.company.Company;
import com.linkedIn.company_job_service.entity.user.User;
import com.linkedIn.company_job_service.enums.JobType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User postedBy;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 150)
    private String position;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Date postedAt;
}
