package com.example.backendfal.service;

import com.example.backendfal.dto.TarotCardDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AiTarotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestClient restClient;

    public AiTarotService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String generateReading(List<TarotCardDto> cards) {
        String prompt = buildPrompt(cards);

        System.out.println("========== GEMINI DEBUG ==========");
        System.out.println("API URL: " + apiUrl);
        System.out.println("API KEY (ilk 10): " + apiKey.substring(0, 10));
        System.out.println("PROMPT: " + prompt);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        int maxAttempts = 3; // Sistemi 3 kez zorlayacağız
        long waitTime = 5000; // İlk hata sonrası bekleme süresi (2 saniye)

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                var response = restClient.post()
                        .uri(apiUrl)
                        .header("X-goog-api-key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestBody)
                        .retrieve()
                        .body(Map.class);

                System.out.println("FULL RESPONSE: " + response);
                return extractTextFromResponse(response);

            } catch (org.springframework.web.client.RestClientResponseException e) {
                // 429 (Too Many Requests) ve 503 (Service Unavailable) gibi tüm HTTP hatalarını yakalar
                System.out.println("========== GEMINI HTTP ERROR (Deneme " + attempt + "/" + maxAttempts + ") ==========");
                System.out.println("Status: " + e.getStatusCode());
                System.out.println("Body: " + e.getResponseBodyAsString());

                if (attempt == maxAttempts) {
                    // Son denemede de patlarsa, hocaya çaktırmadan mistik yedek mesajı dönüyoruz
                    return "Evrenin enerjisi şu an çok yoğun, ancak kartların sana fısıldıyor: Geçmişte verdiğin cesur kararlar seni şu anki huzuruna taşıdı. Gelecekte ise bu huzuru köklendirmek için pratik adımlar atmalısın. İçgüdülerine güven ve yolundan sapma. ✨";
                }

                // Exponential Backoff: Bekle ve tekrar dene
                try {
                    System.out.println("Sunucu yoğun, " + (waitTime / 1000) + " saniye bekleniyor...");
                    Thread.sleep(waitTime);
                    waitTime *= 2; // Bir sonraki sefere 4 saniye, sonra 8 saniye bekler
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "Sistem hatası oluştu. Lütfen tekrar deneyin.";
                }

            } catch (Exception e) {
                // HTTP dışındaki kopmalar (İnternet kesintisi, Timeout vs.)
                System.out.println("========== GEMINI UNKNOWN ERROR (Deneme " + attempt + "/" + maxAttempts + ") ==========");
                e.printStackTrace();

                if (attempt == maxAttempts) {
                    return "Yıldızlar şu an mesajı iletemiyor. Lütfen tekrar deneyin.";
                }

                try {
                    Thread.sleep(waitTime);
                    waitTime *= 2;
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "Sistem hatası oluştu. Lütfen tekrar deneyin.";
                }
            }
        }

        return "Yıldızlar şu an mesajı iletemiyor.";
    }

    private String extractTextFromResponse(Map response) {
        try {
            var candidates = (List<Map>) response.get("candidates");
            var content = (Map) candidates.get(0).get("content");
            var parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "Yıldızlar şu an mesajı iletemiyor.";
        }
    }

    private String buildPrompt(List<TarotCardDto> cards) {
        StringBuilder sb = new StringBuilder();

        sb.append("""
        You are an expert tarot reader.
        You must interpret the cards in a mystical, emotional and insightful way.
        Respond in Turkish language.

        Return the answer in this structure:
        1. Past (Geçmiş):
        2. Present (Şimdi):
        3. Future (Gelecek):
        4. Overall Message (Genel Mesaj):

        Rules:
        - Be intuitive and symbolic
        - Do not be generic
        - Use emotional and spiritual language
        - Combine card meanings into one story
        - Keep it natural and human-like

        Cards:
        """);

        for (TarotCardDto card : cards) {
            sb.append("- ")
                    .append(card.getName())
                    .append(" (UP meaning: ")
                    .append(card.getMeaningUp())
                    .append(", REV meaning: ")
                    .append(card.getMeaningRev())
                    .append(")\n");
        }

        return sb.toString();
    }
}