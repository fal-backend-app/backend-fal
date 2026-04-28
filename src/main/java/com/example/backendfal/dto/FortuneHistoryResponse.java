package com.example.backendfal.dto;

import com.example.backendfal.entity.FortuneType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FortuneHistoryResponse {
    private Long id;
    private FortuneType type;
    private String userInput;
    private String aiResponse;
    private LocalDateTime createdAt;
}