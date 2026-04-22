package com.example.backendfal.controller;

import com.example.backendfal.entity.FortuneResult;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.FortuneResultRepository;
import com.example.backendfal.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 1. Bu sınıfın bir API olduğunu belirtir
@RequestMapping("/api/fortunes") // 2. Ana adresimiz: /api/fortunes/history olacak
public class FortuneController {

    private final FortuneResultRepository fortuneResultRepository;
    private final UserRepository userRepository;

    // 3. Constructor Injection (Repository'lere erişmek için)
    public FortuneController(FortuneResultRepository fortuneResultRepository, UserRepository userRepository) {
        this.fortuneResultRepository = fortuneResultRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/history")
    public List<FortuneResult> getUserHistory() {
        // 4. JWT'den gelen aktif kullanıcı mailini al
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 5. User'ı bul
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 6. DB'den bu kullanıcıya ait falları tarih sırasına göre getir
        return fortuneResultRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
}