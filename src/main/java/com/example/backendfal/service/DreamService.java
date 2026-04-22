package com.example.backendfal.service;

import com.example.backendfal.dto.DreamInterpretRequest;
import com.example.backendfal.dto.DreamInterpretResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DreamService {

    public DreamInterpretResponse interpretDream(DreamInterpretRequest request) {
        String dreamText = request.getDreamText();
        List<String> symbols = request.getSymbols();

        String interpretation = "Rüyan, bilinçaltındaki duygusal geçişleri ve içsel arayışlarını gösteriyor.";
        String suggestion = "Kendini baskı altında hissettiğin konuları sakin şekilde değerlendirebilirsin.";

        if (symbols != null && symbols.contains("su")) {
            interpretation += " Su sembolü duyguları ve iç dünyayı temsil eder.";
        }

        if (symbols != null && symbols.contains("kuş")) {
            interpretation += " Kuş sembolü özgürlük ve haber anlamı taşıyabilir.";
        }

        return new DreamInterpretResponse(
                interpretation,
                List.of("duygular", "değişim", "içsel arayış"),
                suggestion
        );
    }
}