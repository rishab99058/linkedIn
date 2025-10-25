package com.rishab99058.linkedIn.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String profilePictureUrl;
    private String headline;
    @Column(length = 2000)
    private String about;
    private String phoneNumber;
    private String country, state, city, address;
    private String industry;
    private String currentCompany, currentPosition;
    private String webSiteLink;
    private String coverPictureUrl;

    // Use Set here for Hibernate compatibility!
    @ElementCollection
    private Set<String> skills;

    @ElementCollection
    private Set<String> languages;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<Experience> experiences;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<Education> educations;

    @ElementCollection
    private Set<String> certifications;

    @ElementCollection
    private Set<String> projects;

    @ElementCollection
    private Set<String> accomplishments;

    private boolean deleted;
    private boolean deactivated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
