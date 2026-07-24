package com.greenguide.module.ai.service;

import com.greenguide.module.ai.dto.AIResponse;

public interface AIService {

    /** AI 智能垃圾分类问答 */
    AIResponse askGarbageClassification(String question);

    /** AI 出题 */
    AIResponse generateQuizQuestion(String category, String difficulty);

    /** AI 出题（带提示词） */
    AIResponse generateQuizQuestion(String category, String difficulty, String hint);
}
