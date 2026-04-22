package com.example.backendfal.controller;

import com.example.backendfal.dto.DreamInterpretRequest;
import com.example.backendfal.dto.DreamInterpretResponse;
import com.example.backendfal.service.DreamService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dream")
public class DreamController {

    private final DreamService dreamService;

    public DreamController(DreamService dreamService) {
        this.dreamService = dreamService;
    }

    @PostMapping("/interpret")
    public DreamInterpretResponse interpretDream(@RequestBody DreamInterpretRequest request) {
        return dreamService.interpretDream(request);
    }
}