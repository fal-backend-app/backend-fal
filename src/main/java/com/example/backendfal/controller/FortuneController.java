package com.example.backendfal.controller;

import com.example.backendfal.dto.FortuneHistoryResponse;
import com.example.backendfal.entity.FortuneResult;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.FortuneResultRepository;
import com.example.backendfal.repository.UserRepository;
import com.example.backendfal.service.CoffeeFortuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/fortunes")
@RequiredArgsConstructor
public class FortuneController {

    private final FortuneResultRepository fortuneResultRepository;
    private final UserRepository userRepository;
    private final CoffeeFortuneService coffeeFortuneService;

    @GetMapping("/history")
    public List<FortuneHistoryResponse> getUserHistory(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        return fortuneResultRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PostMapping(value = "/coffee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FortuneHistoryResponse interpretCoffeeFortune(
            @RequestParam("cupInside") MultipartFile cupInside,
            @RequestParam("plate") MultipartFile plate,
            @RequestParam("sideAngle") MultipartFile sideAngle,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return coffeeFortuneService.interpretCoffeeFortune(
                email,
                cupInside,
                plate,
                sideAngle
        );
    }

    private FortuneHistoryResponse mapToResponse(FortuneResult result) {
        FortuneHistoryResponse response = new FortuneHistoryResponse();
        response.setId(result.getId());
        response.setType(result.getType());
        response.setUserInput(result.getUserInput());
        response.setAiResponse(result.getAiResponse());
        response.setCreatedAt(result.getCreatedAt());
        return response;
    }
}