package com.linkedIn.company_job_service.entity.job;

import com.linkedIn.company_job_service.entity.user.User;
import com.linkedIn.company_job_service.entity.user.UserFiles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Getter
@Setter

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
)
public class AppliedJobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User appliedBy;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserFiles resume;

    @ManyToOne(optional = false, fetch = FetchType.LAZY
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Job job;

    @Column(nullable = false)
    private Date appliedAt;
}
