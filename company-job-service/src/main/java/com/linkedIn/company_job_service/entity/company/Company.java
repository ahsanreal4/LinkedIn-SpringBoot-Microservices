package com.linkedIn.company_job_service.entity.company;

import com.linkedIn.company_job_service.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column
    private String about;

    @Column(nullable = false)
    private String numEmployees;

    @Column
    private String website;

    @Column(length = 100)
    private String headLine;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private Set<CompanyFiles> files;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private Set<CompanyLocations> locations;
}
