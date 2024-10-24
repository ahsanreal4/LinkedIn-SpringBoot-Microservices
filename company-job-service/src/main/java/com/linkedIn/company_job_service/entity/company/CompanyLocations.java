package com.linkedIn.company_job_service.entity.company;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "location_id"})
)
public class CompanyLocations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @Column()
    private String address;

    @Column(length = 20)
    private String zipCode;

    @Column(nullable = false, length = 40)
    private String city;

    @Column(nullable = false, length = 40)
    private String country;
}
