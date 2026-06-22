package com.example.backendfal.repository;

import com.example.backendfal.entity.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {

    Optional<EmailVerificationCode> findTopByEmailAndCodeAndUsedFalseOrderByCreatedAtDesc(String email, String code);

    Optional<EmailVerificationCode> findTopByEmailAndUsedFalseOrderByCreatedAtDesc(String email);
}