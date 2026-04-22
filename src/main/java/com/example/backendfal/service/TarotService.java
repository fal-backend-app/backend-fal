package com.example.backendfal.service;

import com.example.backendfal.dto.TarotCardDto;
import com.example.backendfal.entity.FortuneResult;
import com.example.backendfal.entity.FortuneType;
import com.example.backendfal.entity.TarotCard;
import com.example.backendfal.entity.User;
import com.example.backendfal.mapper.TarotCardMapper;
import com.example.backendfal.repository.FortuneResultRepository;
import com.example.backendfal.repository.TarotCardRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarotService {

    private final TarotCardRepository repository;
    private final TarotCardMapper mapper;
    private final AiService aiService;
    private final FortuneResultRepository fortuneResultRepository;

    public TarotService(TarotCardRepository repository,
                        TarotCardMapper mapper,
                        AiService aiService,
                        FortuneResultRepository fortuneResultRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.aiService = aiService;
        this.fortuneResultRepository = fortuneResultRepository;
    }

    public List<TarotCardDto> drawCards(int count) {
        List<TarotCard> deck = repository.findAll();
        Collections.shuffle(deck);
        return deck.stream()
                .limit(count)
                .map(mapper::toDto)
                .toList();
    }

    public String interpretCards(List<TarotCardDto> cards, User user) {
        String systemPrompt = "Sen bilge bir Tarot yorumcususun. Sadece Türkçe konuşmalısın. Cevaplarında asla İngilizce kelime veya yabancı karakterler kullanma.";
        String userPrompt = buildPrompt(cards);

        String interpretation = aiService.askAi(systemPrompt, userPrompt);

        saveToDatabase(cards, interpretation, user);

        return interpretation;
    }

    private void saveToDatabase(List<TarotCardDto> cards, String interpretation, User user) {
        FortuneResult result = new FortuneResult();
        result.setType(FortuneType.TAROT);
        result.setUser(user);
        result.setAiResponse(interpretation);

        String cardInput = cards.stream()
                .map(c -> c.getName() + (c.isReversed() ? " (Ters)" : " (Düz)"))
                .collect(Collectors.joining(", "));

        result.setUserInput(cardInput);
        fortuneResultRepository.save(result);
    }

    private String buildPrompt(List<TarotCardDto> cards) {
        StringBuilder sb = new StringBuilder();

        sb.append("""
        You are an expert tarot reader.
        Interpret the cards in a mystical, emotional and insightful way.
        Respond in Turkish.
        IMPORTANT: Pay attention to whether a card is 'UPRIGHT' or 'REVERSED'.

        Structure:
        1. Geçmiş:
        2. Şimdi:
        3. Gelecek:
        4. Genel Mesaj:

        Cards:
        """);

        for (TarotCardDto card : cards) {
            boolean isReversed = Math.random() < 0.25;
            card.setReversed(isReversed);

            sb.append("- ")
                    .append(card.getName())
                    .append(isReversed ? " [REVERSED. Focus: " : " [UPRIGHT. Focus: ")
                    .append(isReversed ? card.getMeaningRev() : card.getMeaningUp())
                    .append("]\n");
        }

        return sb.toString();
    }
}