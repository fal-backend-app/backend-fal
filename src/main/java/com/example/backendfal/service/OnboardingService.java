package com.example.backendfal.service;

import com.example.backendfal.dto.OnboardingRequest;
import com.example.backendfal.dto.OnboardingResponse;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingService {
    private final UserRepository userRepository;

    public OnboardingResponse completeOnboarding(String email, OnboardingRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Kullanıcı bulunamadı:" + email));
        user.setName(request.getName());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());
        user.setRelationshipStatus(request.getRelationshipStatus());
        user.setEmploymentStatus(request.getEmploymentStatus());
        user.setOnboardingCompleted(true);

        User savedUser = userRepository.save(user);

        return OnboardingResponse.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .birthDate(savedUser.getBirthDate())
                .gender(savedUser.getGender())
                .relationshipStatus(savedUser.getRelationshipStatus())
                .employementStatus(savedUser.getEmploymentStatus())
                .onboardingCompleted(savedUser.getOnboardingCompleted())
                .build();
    }
    public OnboardingResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Kullanıcı bulunamadı:" + email));
        return OnboardingResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .relationshipStatus(user.getRelationshipStatus())
                .employementStatus(user.getEmploymentStatus())
                .onboardingCompleted(user.getOnboardingCompleted())
                .build();
    }





}
