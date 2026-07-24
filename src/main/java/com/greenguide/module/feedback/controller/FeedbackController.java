package com.greenguide.module.feedback.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.PageResult;
import com.greenguide.common.Result;
import com.greenguide.module.feedback.entity.Feedback;
import com.greenguide.module.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;

    /** 分页列表 */
    @GetMapping
    public Result<PageResult<Feedback>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String feedbackType,
            @RequestParam(required = false) String status) {
        Page<Feedback> result = service.page(page, size, feedbackType, status);
        return Result.ok(PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords()));
    }

    /** 详情 */
    @GetMapping("/{id}")
    public Result<Feedback> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    /** 处理反馈 */
    @PutMapping("/{id}/process")
    public Result<Void> process(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Long handlerId = Long.parseLong(body.get("handlerId"));
        String handlerNote = body.get("handlerNote");
        String status = body.getOrDefault("status", "PROCESSED");
        service.process(id, handlerId, handlerNote, status);
        return Result.ok();
    }
}
