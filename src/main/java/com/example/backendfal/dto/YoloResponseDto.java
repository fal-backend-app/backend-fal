package com.example.backendfal.dto;

import java.util.List;

public record YoloResponseDto(
        List<YoloDetectionDto> detections) {
}