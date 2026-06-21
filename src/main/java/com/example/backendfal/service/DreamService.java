package com.example.backendfal.service;

import com.example.backendfal.dto.DreamInterpretRequest;
import com.example.backendfal.dto.DreamInterpretResponse;
import com.example.backendfal.entity.FortuneResult;
import com.example.backendfal.entity.FortuneType;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.FortuneResultRepository;
import com.example.backendfal.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DreamService {

    private final AiService aiService;
    private final FortuneResultRepository fortuneResultRepository;
    private final UserRepository userRepository;

    public DreamService(AiService aiService,
                        FortuneResultRepository fortuneResultRepository,
                        UserRepository userRepository) {
        this.aiService = aiService;
        this.fortuneResultRepository = fortuneResultRepository;
        this.userRepository = userRepository;
    }

    public DreamInterpretResponse interpretDream(DreamInterpretRequest request) {
        // 1. Kullanıcıyı bul
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        // 2. Tarot'taki mistik tonu koruyan Prompt yapısı
        String systemPrompt = """
            You are an expert dream interpreter and psychologist.
            Interpret the dreams in a mystical, emotional and insightful way.
            Respond in Turkish.
            
            Structure:
            1. Rüya Analizi:
            2. Sembollerin Dili:
            3. Bilinçaltı Mesajı:
            4. Genel Tavsiye:
            """;

        String userPrompt = "Interpret this dream: " + request.getDreamText();

        String interpretation = aiService.askAi(systemPrompt, userPrompt);

        saveToDatabase(request.getDreamText(), interpretation, user);

        return new DreamInterpretResponse(
                interpretation,
                List.of("Bilinçaltı Analizi"),
                "Rüyanızın rehberliğine güvenin."
        );
    }

    private void saveToDatabase(String dreamText, String interpretation, User user) {
        FortuneResult result = new FortuneResult();
        result.setType(FortuneType.DREAM);
        result.setUser(user);
        result.setUserInput(dreamText);
        result.setAiResponse(interpretation);
        fortuneResultRepository.save(result);
    }
}