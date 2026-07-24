package com.greenguide.module.quiz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.PageResult;
import com.greenguide.common.Result;
import com.greenguide.module.quiz.entity.QuizQuestion;
import com.greenguide.module.quiz.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/quiz")
@RequiredArgsConstructor
public class QuizQuestionController {

    private final QuizQuestionService service;

    /** 分页列表 */
    @GetMapping
    public Result<PageResult<QuizQuestion>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String categoryTag,
            @RequestParam(required = false) String status) {
        Page<QuizQuestion> result = service.page(page, size, difficulty, categoryTag, status);
        return Result.ok(PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords()));
    }

    /** 详情 */
    @GetMapping("/{id}")
    public Result<QuizQuestion> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Void> create(@RequestBody QuizQuestion question) {
        service.create(question);
        return Result.ok();
    }

    /** 编辑 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody QuizQuestion question) {
        question.setId(id);
        service.update(question);
        return Result.ok();
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }

    /** 发布 */
    @PutMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        service.publish(id);
        return Result.ok();
    }
}
