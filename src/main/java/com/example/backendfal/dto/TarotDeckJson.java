package com.example.backendfal.dto;

import java.util.List;

public class TarotDeckJson {

    private List<TarotCardJson> cards;

    public List<TarotCardJson> getCards() {
        return cards;
    }

    public void setCards(List<TarotCardJson> cards) {
        this.cards = cards;
    }
}