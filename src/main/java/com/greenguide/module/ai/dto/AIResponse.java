package com.greenguide.module.ai.dto;

import lombok.Data;
import java.util.Map;

@Data
public class AIResponse {
    private String answer;
    private String classification;
    private String disposalGuide;
    private Map<String, Object> extra;
}
