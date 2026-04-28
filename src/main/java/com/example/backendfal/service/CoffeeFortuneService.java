package com.example.backendfal.service;

import com.example.backendfal.dto.FortuneHistoryResponse;
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

    public FortuneHistoryResponse interpretCoffeeFortune(
            String email,
            MultipartFile cupInside,
            MultipartFile plate,
            MultipartFile sideAngle
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        validateImages(cupInside, plate, sideAngle);

        FortuneResult result = new FortuneResult();
        result.setUser(user);
        result.setType(FortuneType.COFFEE);
        result.setUserInput("Fincan İçi, Tabak ve Yan Açı fotoğrafları yüklendi.");
        result.setAiResponse(createMockCoffeeInterpretation());
        result.setCreatedAt(LocalDateTime.now());

        FortuneResult saved = fortuneResultRepository.save(result);

        return mapToResponse(saved);
    }

    private void validateImages(
            MultipartFile cupInside,
            MultipartFile plate,
            MultipartFile sideAngle
    ) {
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

    private String createMockCoffeeInterpretation() {
        return """
                Kahve falında belirgin bir yol ve açılan bir kapı görünüyor.
                Bu, yakın zamanda hayatında yeni bir başlangıç yapabileceğini gösterir.

                Fincanın içindeki yoğunluk, son dönemde zihnini meşgul eden bazı konular olduğunu anlatıyor.
                Ancak tabakta görünen açıklık, bu sıkışıklığın yavaş yavaş dağılacağına işaret eder.

                Yakın çevrenden gelecek bir haber seni rahatlatabilir.
                Özellikle iş, okul veya kişisel hedeflerinle ilgili beklediğin bir gelişme olabilir.

                Genel olarak bu fal, sabırlı kalman gerektiğini ama önünde güzel bir fırsatın açılacağını söylüyor.
                """;
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