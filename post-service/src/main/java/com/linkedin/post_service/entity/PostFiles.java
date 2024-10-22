package com.linkedin.post_service.entity;

import com.linkedin.post_service.enums.PostFileType;
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
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "type"})
)
public class PostFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostFileType type;

    @Column(nullable = false)
    private String link;
}
