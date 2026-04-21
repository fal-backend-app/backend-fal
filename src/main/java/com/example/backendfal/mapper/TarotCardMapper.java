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
    public TarotCardDto toDto(TarotCard card) {
        TarotCardDto dto = new TarotCardDto();
        dto.setId(card.getId());
        dto.setName(card.getName());
        dto.setNameShort(card.getNameShort());

        if (card.getNameShort() != null) {
            String shortName = card.getNameShort();
            String fileName = shortName;

            // Senin klasöründeki isimlendirme mantığı (m01, c01 vb.)
            if (shortName.startsWith("ar")) {
                fileName = shortName.replace("ar", "m");
            } else if (shortName.startsWith("cu")) {
                fileName = shortName.replace("cu", "c");
            } else if (shortName.startsWith("sw")) {
                fileName = shortName.replace("sw", "s");
            } else if (shortName.startsWith("wa")) {
                fileName = shortName.replace("wa", "w");
            } else if (shortName.startsWith("pe")) {
                fileName = shortName.replace("pe", "p");
            }

            // iOS Simülatörü için localhost kullanılır
            String imageUrl = "http://localhost:8080/images/" + fileName + ".jpg";
            dto.setImageUrl(imageUrl);
        }

        dto.setMeaningUp(card.getMeaningUp());
        dto.setMeaningRev(card.getMeaningRev());
        dto.setDescription(card.getDescription());
        return dto;
    }

}