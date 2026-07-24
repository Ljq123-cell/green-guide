package com.greenguide.module.quiz.controller;

import com.greenguide.common.Result;
import com.greenguide.module.quiz.mapper.QuizRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class LeaderboardController {

    private final QuizRecordMapper quizRecordMapper;

    /** 答题排行榜（按正确数降序） */
    @GetMapping("/leaderboard")
    public Result<List<Map<String, Object>>> leaderboard() {
        // 聚合统计：按 correct_count 降序
        List<Map<String, Object>> raw = quizRecordMapper.selectMaps(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.greenguide.module.quiz.entity.QuizRecord>()
                        .select("user_id", "COUNT(*) AS total",
                                "SUM(CASE WHEN is_correct = 1 THEN 1 ELSE 0 END) AS correct")
                        .groupBy("user_id")
                        .orderByDesc("correct")
                        .last("LIMIT 20"));

        List<Map<String, Object>> result = new ArrayList<>();
        int rank = 1;
        for (Map<String, Object> row : raw) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("rank", rank++);
            Object userId = row.get("user_id");
            entry.put("userId", userId != null ? Long.parseLong(userId.toString()) : 0);
            entry.put("total", row.get("total"));
            entry.put("correct", row.get("correct"));
            long total = ((Number) row.get("total")).longValue();
            long correct = ((Number) row.get("correct")).longValue();
            entry.put("accuracy", total > 0 ? Math.round(correct * 100.0 / total) : 0);
            result.add(entry);
        }
        return Result.ok(result);
    }
}
