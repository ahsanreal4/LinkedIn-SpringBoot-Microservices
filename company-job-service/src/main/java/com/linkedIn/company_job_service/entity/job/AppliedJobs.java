package com.linkedIn.company_job_service.entity.job;

import com.linkedIn.company_job_service.entity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
)
public class AppliedJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;

    @Column(nullable = false)
    private Date appliedDate;
}
