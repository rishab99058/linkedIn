package com.rishab99058.linkedIn.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "user_id")
    private String userId;
    private String school;
    private String degree;
    private String field;
    private LocalDate startDate;
    private LocalDate endDate;
    private String grade;
    @Column(length = 1000)
    private String description;
}

