package com.greenguide.module.learning.controller;

import com.greenguide.common.Result;
import com.greenguide.module.learning.service.LearningService;
import com.greenguide.security.AdminUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    /** 获取用户学习统计 (个人中心用) */
    @GetMapping("/user/{userId}/stats")
    public Result<Map<String, Object>> stats(@PathVariable Long userId) {
        return Result.ok(learningService.getUserStats(userId));
    }

    /** 当前用户的统计 */
    @GetMapping("/user/stats")
    public Result<Map<String, Object>> myStats(@AuthenticationPrincipal AdminUserDetails principal) {
        return Result.ok(learningService.getUserStats(principal.getId()));
    }
}
