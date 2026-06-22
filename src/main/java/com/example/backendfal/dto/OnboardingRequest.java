package com.example.backendfal.dto;

import com.example.backendfal.entity.EmploymentStatus;
import com.example.backendfal.entity.Gender;
import com.example.backendfal.entity.RelationshipStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OnboardingRequest {

    @NotBlank(message = "İsim boş olamaz")
    private String name;

    @NotNull(message = "Doğum tarihi boş olamaz")
    private LocalDate birthDate;

    @NotNull(message = "Cinsiyet boş olamaz")
    private Gender gender;

    @NotNull(message = "İlişki durumu boş olamaz")
    private RelationshipStatus relationshipStatus;

    @NotNull(message = "İş durumu boş olamaz")
    private EmploymentStatus employmentStatus;
}