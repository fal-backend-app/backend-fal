package com.example.backendfal.service;

import com.example.backendfal.dto.FortuneHistoryResponse;
import com.example.backendfal.dto.YoloResponseDto;
import com.example.backendfal.entity.FortuneResult;
import com.example.backendfal.entity.FortuneType;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.FortuneResultRepository;
import com.example.backendfal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CoffeeFortuneService {

    private final UserRepository userRepository;
    private final FortuneResultRepository fortuneResultRepository;
    private final YoloService yoloService;
    private final AiService aiService;

    public FortuneHistoryResponse interpretCoffeeFortune(
            String email,
            MultipartFile cupInside,
            MultipartFile plate,
            MultipartFile sideAngle) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        validateImages(cupInside, plate, sideAngle);

        YoloResponseDto cupInsideResult = yoloService.detectSymbols(cupInside);
        YoloResponseDto plateResult = yoloService.detectSymbols(plate);
        YoloResponseDto sideAngleResult = yoloService.detectSymbols(sideAngle);

        String systemPrompt = buildCoffeeSystemPrompt();
        String userPrompt = buildCoffeePrompt(cupInsideResult, plateResult, sideAngleResult);

        String aiResponse = aiService.askAi(systemPrompt, userPrompt);
        FortuneResult result = new FortuneResult();
        result.setUser(user);
        result.setType(FortuneType.COFFEE);
        result.setUserInput(userPrompt);
        result.setAiResponse(aiResponse);
        result.setCreatedAt(LocalDateTime.now());

        FortuneResult saved = fortuneResultRepository.save(result);

        return mapToResponse(saved);
    }

    private String buildCoffeeSystemPrompt() {
        return """
                Sen mistik, sezgisel ve yaratıcı bir Türk kahvesi falı yorumcususun.
                Kullanıcıya Türk kahvesi falı üslubuyla, samimi ve doğal şekilde yorum yap.
                Teknik terimler kullanma.
                YOLO, model, confidence, tespit, analiz gibi teknik kelimeleri kullanıcıya söyleme.
                Yorumu Türkçe yaz.
                Her falda aynı kalıp cümleleri tekrar etme.
                2 veya 3 paragraf yaz.
                Kullanıcıya direkt ve samimi konuş.
                """;
    }

    private void validateImages(
            MultipartFile cupInside,
            MultipartFile plate,
            MultipartFile sideAngle) {
        if (cupInside == null || cupInside.isEmpty()) {
            throw new IllegalArgumentException("Fincan içi fotoğrafı zorunludur.");
        }

        if (plate == null || plate.isEmpty()) {
            throw new IllegalArgumentException("Tabak fotoğrafı zorunludur.");
        }

        if (sideAngle == null || sideAngle.isEmpty()) {
            throw new IllegalArgumentException("Yan açı fotoğrafı zorunludur.");
        }
    }

    private String buildCoffeePrompt(
            YoloResponseDto cupInside,
            YoloResponseDto plate,
            YoloResponseDto sideAngle) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    private void appendDetections(StringBuilder sb, YoloResponseDto result) {
        if (result == null || result.detections() == null || result.detections().isEmpty()) {
            sb.append("- Belirgin sembol tespit edilemedi.\n");
            return;
        }

        result.detections().forEach(detection -> {
            sb.append("- ")
                    .append(detection.className())
                    .append(" | güven: ")
                    .append(detection.confidence())
                    .append("\n");
        });
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