package com.greenguide.module.feedback.controller;

import com.greenguide.common.Result;
import com.greenguide.module.feedback.entity.Feedback;
import com.greenguide.module.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/feedback")
@RequiredArgsConstructor
public class PublicFeedbackController {

    private final FeedbackService service;

    /** 用户提交分类纠错/建议 */
    @PostMapping
    public Result<Void> submit(@RequestBody Feedback feedback) {
        service.submit(feedback);
        return Result.ok();
    }
}
