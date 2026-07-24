package com.greenguide.module.quiz.controller;

import com.greenguide.common.Result;
import com.greenguide.module.quiz.entity.QuizQuestion;
import com.greenguide.module.quiz.service.QuizQuestionService;
import com.greenguide.module.quiz.service.QuizRecordService;
import com.greenguide.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/quiz")
@RequiredArgsConstructor
public class PublicQuizController {

    private final QuizQuestionService questionService;
    private final QuizRecordService recordService;

    @GetMapping("/random")
    public Result<List<QuizQuestion>> random(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) String difficulty) {
        return Result.ok(questionService.randomQuestions(count, difficulty));
    }

    @PostMapping("/submit")
    public Result<Map<String, Object>> submit(@RequestBody Map<Long, String> answers) {
        Long userId = getCurrentUserId();
        return Result.ok(recordService.submitAnswers(userId, answers));
    }

    @GetMapping("/stats/{userId}")
    public Result<Map<String, Object>> stats(@PathVariable Long userId) {
        return Result.ok(recordService.userStats(userId));
    }

    /** 从认证上下文中获取当前用户 ID，未登录返回 0 */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getId();
        }
        if (auth != null && auth.getPrincipal() instanceof com.greenguide.security.AdminUserDetails ad) {
            return ad.getId();
        }
        return 0L;
    }
}
