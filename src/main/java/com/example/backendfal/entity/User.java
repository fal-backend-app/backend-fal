package com.example.backendfal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Onboarding alanları
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private RelationshipStatus relationshipStatus;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @Builder.Default
    @Column(nullable = false)
    private Boolean onboardingCompleted = false;
}