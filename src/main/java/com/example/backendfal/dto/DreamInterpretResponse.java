package com.example.backendfal.dto;

import java.util.List;

public class DreamInterpretResponse {
    private String interpretation;
    private List<String> detectedThemes;
    private String suggestion;

    public DreamInterpretResponse() {
    }

    public DreamInterpretResponse(String interpretation, List<String> detectedThemes, String suggestion) {
        this.interpretation = interpretation;
        this.detectedThemes = detectedThemes;
        this.suggestion = suggestion;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public List<String> getDetectedThemes() {
        return detectedThemes;
    }

    public void setDetectedThemes(List<String> detectedThemes) {
        this.detectedThemes = detectedThemes;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}