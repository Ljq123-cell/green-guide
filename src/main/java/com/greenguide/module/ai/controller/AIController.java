package com.greenguide.module.ai.controller;

import com.greenguide.common.Result;
import com.greenguide.module.ai.dto.AIResponse;
import com.greenguide.module.ai.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    /** AI 智能分类问答 */
    @PostMapping("/classify")
    public Result<AIResponse> classify(@RequestBody Map<String, String> body) {
        String question = body.getOrDefault("question", "");
        if (question.isBlank()) {
            return Result.badRequest("请输入要查询的垃圾名称");
        }
        return Result.ok(aiService.askGarbageClassification(question));
    }

    /** AI 生成题目 */
    @PostMapping("/generate-quiz")
    public Result<AIResponse> generateQuiz(@RequestBody Map<String, Object> body) {
        String category = body.getOrDefault("category", "").toString();
        String difficulty = body.getOrDefault("difficulty", "BEGINNER").toString();
        String hint = body.getOrDefault("hint", "").toString();

        // 如果前端提供了提示词，传递给service层
        if (!hint.isEmpty()) {
            return Result.ok(aiService.generateQuizQuestion(category, difficulty, hint));
        }
        return Result.ok(aiService.generateQuizQuestion(category, difficulty));
    }
}
