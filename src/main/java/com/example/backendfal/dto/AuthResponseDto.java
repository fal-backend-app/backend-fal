package com.example.backendfal.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private String token;
    private String message;
    private boolean onboardingCompleted;
}