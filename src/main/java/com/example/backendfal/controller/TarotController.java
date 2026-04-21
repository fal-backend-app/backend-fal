package com.example.backendfal.controller;

import com.example.backendfal.dto.TarotCardDto;
import com.example.backendfal.dto.TarotResponse;
import com.example.backendfal.service.TarotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarot")
@CrossOrigin(origins = "*")
public class TarotController {

    private final TarotService tarotService;

    public TarotController(TarotService tarotService) {
        this.tarotService = tarotService;
    }

    @GetMapping("/draw")
    public TarotResponse draw() {

        List<TarotCardDto> cards = tarotService.drawCards(3);

        String interpretation = tarotService.interpretCards(cards);

        return new TarotResponse(cards, interpretation);
    }

}