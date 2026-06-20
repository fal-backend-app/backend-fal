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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        int age = java.time.Period.between(user.getBirthDate(), java.time.LocalDate.now()).getYears();

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

        // Kullanıcı bağlamını rüya metniyle birleştiriyoruz
        String userPrompt = String.format("""
        User Profile: %d years old, %s, %s, %s.
        Interpret this dream: %s
        """,
                age,
                user.getGender(),
                user.getRelationshipStatus(),
                user.getEmploymentStatus(),
                request.getDreamText());

        String interpretation = aiService.askAi(systemPrompt, userPrompt);
        saveToDatabase(request.getDreamText(), interpretation, user);

        return new DreamInterpretResponse(
                interpretation,
                List.of("Kişiye Özel Analiz"),
                "Rüyanızın rehberliği sizin yaşam yolculuğunuza ışık tutuyor."
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