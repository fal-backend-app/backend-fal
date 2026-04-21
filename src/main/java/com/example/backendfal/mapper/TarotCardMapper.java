package com.example.backendfal.mapper;

import com.example.backendfal.dto.TarotCardDto;
import com.example.backendfal.dto.TarotCardJson;
import com.example.backendfal.entity.TarotCard;
import org.springframework.stereotype.Component;

@Component
public class TarotCardMapper {

    // JSON → Entity
    public TarotCard toEntity(TarotCardJson json) {
        return TarotCard.builder()
                .name(json.getName())
                .nameShort(json.getNameShort())
                .meaningUp(json.getMeaningUp())
                .meaningRev(json.getMeaningRev())
                .description(json.getDesc())
                .build();
    }

    // Entity → DTO
    // Entity → DTO
    public TarotCardDto toDto(TarotCard card) {
        TarotCardDto dto = new TarotCardDto();
        dto.setId(card.getId());
        dto.setName(card.getName());
        dto.setNameShort(card.getNameShort());

        if (card.getNameShort() != null && !card.getNameShort().isEmpty()) {
            String shortName = card.getNameShort();
            String fileName = shortName;

            // 1. AŞAMA: Takımları (Harfleri) klasöründeki gibi tek harfe çevir
            if (fileName.startsWith("ar")) {
                fileName = fileName.replace("ar", "m");
            } else if (fileName.startsWith("cu")) {
                fileName = fileName.replace("cu", "c");
            } else if (fileName.startsWith("sw")) {
                fileName = fileName.replace("sw", "s");
            } else if (fileName.startsWith("wa")) {
                fileName = fileName.replace("wa", "w");
            } else if (fileName.startsWith("pe")) {
                fileName = fileName.replace("pe", "p");
            }

            // 2. AŞAMA: As ve Saray kartlarının harflerini sayılara çevir
            if (fileName.endsWith("ac")) {
                fileName = fileName.replace("ac", "01"); // As (Ace) -> 01
            } else if (fileName.endsWith("pa")) {
                fileName = fileName.replace("pa", "11"); // Prens (Page) -> 11
            } else if (fileName.endsWith("kn")) {
                fileName = fileName.replace("kn", "12"); // Şövalye (Knight) -> 12
            } else if (fileName.endsWith("qu")) {
                fileName = fileName.replace("qu", "13"); // Kraliçe (Queen) -> 13
            } else if (fileName.endsWith("ki")) {
                fileName = fileName.replace("ki", "14"); // Kral (King) -> 14
            }

            // iOS güvenlik duvarına (ATS) takılmamak için 127.0.0.1
            String imageUrl = "http://127.0.0.1:8080/images/" + fileName + ".jpg";
            dto.setImageUrl(imageUrl);
        }

        dto.setMeaningUp(card.getMeaningUp());
        dto.setMeaningRev(card.getMeaningRev());
        dto.setDescription(card.getDescription());
        return dto;
    }
}