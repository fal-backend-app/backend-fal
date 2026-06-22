package com.example.backendfal.controller;

import com.example.backendfal.dto.*;
import com.example.backendfal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/request")
    public MessageResponseDto registerRequest(@Valid @RequestBody RegisterRequestDto request) {
        return authService.registerRequest(request);
    }

    @PostMapping("/register/verify")
    public AuthResponseDto verifyRegisterCode(@Valid @RequestBody VerifyCodeRequestDto request) {
        return authService.verifyRegisterCode(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/resend-code")
    public MessageResponseDto resendCode(@RequestParam String email) {
        return authService.resendCode(email);
    }

    @PostMapping("/google-login")
    public AuthResponseDto googleLogin(@Valid @RequestBody GoogleLoginRequestDto request) {
        return authService.googleLogin(request);
    }
}