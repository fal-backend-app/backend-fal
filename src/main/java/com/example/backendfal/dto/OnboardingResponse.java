package com.example.backendfal.dto;

import com.example.backendfal.entity.EmploymentStatus;
import com.example.backendfal.entity.Gender;
import com.example.backendfal.entity.RelationshipStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class OnboardingResponse {
    private Long userId;
    private String name;
    private String email;
    private LocalDate birthDate;
    private Gender gender;
    private RelationshipStatus relationshipStatus;
    private EmploymentStatus employementStatus;
    private Boolean onboardingCompleted;
}
