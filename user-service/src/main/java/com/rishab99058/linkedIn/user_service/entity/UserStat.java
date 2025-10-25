package com.rishab99058.linkedIn.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserStat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;  // reference to UserEntity

    private int connectionsCount;
    private int followersCount;
    private int postsCount;
    private int experiencesCount;
    private int educationsCount;
    private int skillsCount;
    private int projectsCount;
    private int certificationsCount;
    private int accomplishmentsCount;
    private int profileCompletionPercent;

    // Add any other analytic/aggregate fields you want

    private boolean deleted = false;
}
