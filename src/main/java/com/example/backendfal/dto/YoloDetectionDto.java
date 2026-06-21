package com.example.backendfal.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record YoloDetectionDto(
                @JsonAlias({
                                "classId", "class_id" }) Integer classId,

                @JsonAlias({ "className", "class_name" }) String className,

                Double confidence,
                Box box) {
        public record Box(
                        Double x1,
                        Double y1,
                        Double x2,
                        Double y2) {
        }
}