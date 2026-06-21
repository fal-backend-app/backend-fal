package com.example.backendfal.service;

import com.example.backendfal.dto.YoloResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class YoloService {

    @Value("${ai.yolo.url}")
    private String yoloUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public YoloResponseDto detectSymbols(MultipartFile image) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            ByteArrayResource imageResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename() != null
                            ? image.getOriginalFilename()
                            : "image.jpg";
                }
            };

            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", imageResource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    yoloUrl,
                    requestEntity,
                    String.class);

            System.out.println("YOLO RESPONSE: " + response.getBody());

            return objectMapper.readValue(response.getBody(), YoloResponseDto.class);

        } catch (Exception e) {
            throw new RuntimeException("YOLO servis çağrısı başarısız: " + e.getMessage(), e);
        }
    }
}