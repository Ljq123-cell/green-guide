package com.greenguide.module.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.PageResult;
import com.greenguide.common.Result;
import com.greenguide.module.knowledge.entity.KnowledgeArticle;
import com.greenguide.module.knowledge.service.KnowledgeArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/knowledge")
@RequiredArgsConstructor
public class PublicKnowledgeController {

    private final KnowledgeArticleService service;

    /** 公开文章列表（分页，仅已发布） */
    @GetMapping
    public Result<PageResult<KnowledgeArticle>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        Page<KnowledgeArticle> result = service.publicPage(page, size, category, keyword);
        return Result.ok(PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords()));
    }

    /** 公开文章详情（自动增加浏览量） */
    @GetMapping("/{id}")
    public Result<KnowledgeArticle> detail(@PathVariable Long id) {
        return Result.ok(service.publicGetById(id));
    }
}
