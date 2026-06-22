package com.example.backendfal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String modelName;

    private final RestClient restClient;

    public AiService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public String askAi(String systemPrompt, String userPrompt) {
        Map<String, Object> requestBody = Map.of(
                "model", modelName,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.5
        );

        try {
            var response = restClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            return extractText(response);
        } catch (Exception e) {
            System.err.println("Groq Error: " + e.getMessage());
            return "Yıldızlar şu an mesajı iletemiyor, ancak içindeki güce güven. ✨";
        }
    }

    private String extractText(Map response) {
        try {
            var choices = (List<Map>) response.get("choices");
            var message = (Map) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Yorum çözümlenirken bir hata oluştu.";
        }
    }
}