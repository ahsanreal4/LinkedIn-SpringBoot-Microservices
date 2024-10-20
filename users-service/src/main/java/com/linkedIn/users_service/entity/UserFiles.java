package com.linkedIn.users_service.entity;

import com.linkedIn.users_service.enums.UserFileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "file_id"})
)
public class UserFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserFileType type;

    @Column(nullable = false, length = 10)
    private String extension;

    @Column(nullable = false)
    private Float sizeInMb;

    @Column(nullable = false)
    private String link;
}
