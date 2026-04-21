package com.example.backendfal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarotCardDto {

    private Long id;
    private String name;
    private String nameShort;
    private String imageUrl;
    private String meaningUp;
    private String meaningRev;
    private String description;
}