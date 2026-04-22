package com.example.backendfal.controller;

import com.example.backendfal.dto.TarotCardDto;
import com.example.backendfal.dto.TarotResponse;
import com.example.backendfal.entity.User;
import com.example.backendfal.repository.UserRepository;
import com.example.backendfal.service.TarotService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarot")
@CrossOrigin(origins = "*")
public class TarotController {

    private final TarotService tarotService;
    private final UserRepository userRepository; // Kullanıcıyı bulmak için ekledik

    public TarotController(TarotService tarotService, UserRepository userRepository) {
        this.tarotService = tarotService;
        this.userRepository = userRepository;
    }

    @GetMapping("/draw")
    public TarotResponse draw() {

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();


        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        List<TarotCardDto> cards = tarotService.drawCards(3);

        String interpretation = tarotService.interpretCards(cards, user);

        return new TarotResponse(cards, interpretation);
    }
}