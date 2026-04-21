package com.example.backendfal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tarot_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarotCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nameShort;

    @Column(length = 3000)
    private String meaningUp;

    @Column(length = 3000)
    private String meaningRev;

    @Column(length = 5000)
    private String description;
}