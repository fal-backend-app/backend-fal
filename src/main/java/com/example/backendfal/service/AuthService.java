package com.example.backendfal.service;

import com.example.backendfal.dto.*;
import com.example.backendfal.entity.EmailVerificationCode;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.EmailVerificationCodeRepository;
import com.example.backendfal.repository.UserRepository;
import jdk.jfr.DataAmount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       EmailVerificationCodeRepository verificationCodeRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    @Value("${app.verification.code-expiration-minutes}")
    private long codeExpirationMinutes;

    public MessageResponseDto registerRequest(RegisterRequestDto request) {
        String email = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(email)) {
            User existingUser = userRepository.findByEmail(email).orElseThrow();

            if (Boolean.TRUE.equals(existingUser.getEmailVerified())) {
                throw new RuntimeException("Bu email zaten kayıtlı");
            }

            existingUser.setName(request.getName());
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(existingUser);
        } else {
            User newUser = User.builder()
                    .name(request.getName())
                    .email(email)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .enabled(false)
                    .emailVerified(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(newUser);
        }

        String code = generateVerificationCode();

        EmailVerificationCode verificationCode = EmailVerificationCode.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(codeExpirationMinutes))
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        verificationCodeRepository.save(verificationCode);
        emailService.sendVerificationCode(email, code);

        return new MessageResponseDto("Doğrulama kodu email adresine gönderildi");
    }

    public AuthResponseDto verifyRegisterCode(VerifyCodeRequestDto request) {
        String email = request.getEmail().toLowerCase().trim();

        EmailVerificationCode verificationCode = verificationCodeRepository
                .findTopByEmailAndCodeAndUsedFalseOrderByCreatedAtDesc(email, request.getCode())
                .orElseThrow(() -> new RuntimeException("Kod yanlış veya bulunamadı"));

        if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Kodun süresi dolmuş");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setEnabled(true);
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .message("Email doğrulandı, kayıt tamamlandı")
                .build();
    }

    public AuthResponseDto login(LoginRequestDto request) {
        String email = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email veya şifre hatalı"));

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Önce email adresinizi doğrulamanız gerekiyor");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email veya şifre hatalı");
        }

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .message("Giriş başarılı")
                .onboardingCompleted(user.getOnboardingCompleted())
                .build();
    }

    public MessageResponseDto resendCode(String emailInput) {
        String email = emailInput.toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Bu email zaten doğrulanmış");
        }

        String code = generateVerificationCode();

        EmailVerificationCode verificationCode = EmailVerificationCode.builder()
                .email(email)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(codeExpirationMinutes))
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        verificationCodeRepository.save(verificationCode);
        emailService.sendVerificationCode(email, code);

        return new MessageResponseDto("Yeni doğrulama kodu gönderildi");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    public AuthResponseDto googleLogin(GoogleLoginRequestDto request) {
        String email = request.getEmail().toLowerCase().trim();
        String name = request.getName().trim();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(name)
                            .email(email)
                            .password("")   // boş string
                            .enabled(true)
                            .emailVerified(true)
                            .createdAt(LocalDateTime.now())
                            .build();

                    return userRepository.save(newUser);
                });

        // kullanıcı zaten varsa bilgilerini güncelle
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(name);
        }

        user.setEnabled(true);
        user.setEmailVerified(true);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .message("Google ile giriş başarılı")
                .build();
    }
}