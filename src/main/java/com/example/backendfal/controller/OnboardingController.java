package com.example.backendfal.controller;

import com.example.backendfal.dto.OnboardingRequest;
import com.example.backendfal.dto.OnboardingResponse;
import com.example.backendfal.service.OnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    @PostMapping
    public OnboardingResponse completeOnboarding(@Valid @RequestBody OnboardingRequest request,
                                                 Authentication authentication){
        String email = authentication.getName();
        return  onboardingService.completeOnboarding(email, request);
    }

    @GetMapping("/me")
    public OnboardingResponse getMyProfile(Authentication authentication){
        String email = authentication.getName();
        return onboardingService.getMyProfile(email);
    }
}
