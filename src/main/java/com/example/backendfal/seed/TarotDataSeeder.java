package com.example.backendfal.seed;

import com.example.backendfal.dto.TarotDeckJson;
import com.example.backendfal.entity.TarotCard;
import com.example.backendfal.mapper.TarotCardMapper;
import com.example.backendfal.repository.TarotCardRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper; // Import duruyor
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class TarotDataSeeder implements ApplicationRunner {

    private final TarotCardRepository repository;
    private final TarotCardMapper mapper;
    // ObjectMapper'ı Spring'den istemiyoruz, kendimiz oluşturuyoruz
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Constructor'dan objectMapper parametresini kaldırdık
    public TarotDataSeeder(TarotCardRepository repository,
                           TarotCardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (repository.count() > 0) {
            System.out.println("DB zaten dolu, seed atlanıyor.");
            return;
        }

        InputStream inputStream =
                getClass().getResourceAsStream("/data/card_data.json");

        if (inputStream == null) {
            System.err.println("HATA: /src/main/resources/data/card_data.json dosyası bulunamadı!");
            return;
        }

        try {
            TarotDeckJson deck = objectMapper.readValue(inputStream, TarotDeckJson.class);

            List<TarotCard> cards = deck.getCards()
                    .stream()
                    .map(mapper::toEntity)
                    .toList();

            repository.saveAll(cards);
            System.out.println("Tarot kartları yüklendi: " + cards.size());
        } catch (Exception e) {
            System.err.println("JSON okuma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}