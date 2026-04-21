package com.example.backendfal.service;

import com.example.backendfal.dto.TarotCardDto;
import com.example.backendfal.entity.TarotCard;
import com.example.backendfal.mapper.TarotCardMapper;
import com.example.backendfal.repository.TarotCardRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TarotService {

    private final TarotCardRepository repository;
    private final TarotCardMapper mapper;
    private final AiTarotService aiTarotService;

    public TarotService(TarotCardRepository repository,
                        TarotCardMapper mapper,
                        AiTarotService aiTarotService) {
        this.repository = repository;
        this.mapper = mapper;
        this.aiTarotService = aiTarotService;
    }

    public List<TarotCardDto> drawCards(int count) {

        List<TarotCard> deck = repository.findAll();

        Collections.shuffle(deck);

        return deck.stream()
                .limit(count)
                .map(mapper::toDto)
                .toList();
    }

    public String interpretCards(List<TarotCardDto> cards) {
        return aiTarotService.generateReading(cards);
    }
}