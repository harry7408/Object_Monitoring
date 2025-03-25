package com.harry.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FastApiResponse {

    private List<ObjectDto> result;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectDto {
        private String item;
        private Double confidence;
        private Double left;
        private Double top;
        private Double right;
        private Double bottom;
    }
}


