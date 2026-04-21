package com.example.backendfal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarotCardJson {

    private String type;

    @JsonProperty("name_short")
    private String nameShort;

    @JsonProperty("name")
    private String name;

    @JsonProperty("meaning_up")
    private String meaningUp;

    @JsonProperty("meaning_rev")
    private String meaningRev;

    @JsonProperty("desc")
    private String desc;
}