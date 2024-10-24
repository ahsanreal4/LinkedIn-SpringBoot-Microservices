package com.linkedIn.company_job_service.entity.company;

import com.linkedIn.company_job_service.enums.CompanyFileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "type"})
)
public class CompanyFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyFileType type;

    @Column(nullable = false)
    private String link;
}
