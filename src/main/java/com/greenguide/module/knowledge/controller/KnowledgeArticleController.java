package com.greenguide.module.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.PageResult;
import com.greenguide.common.Result;
import com.greenguide.module.knowledge.entity.KnowledgeArticle;
import com.greenguide.module.knowledge.service.KnowledgeArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/knowledge")
@RequiredArgsConstructor
public class KnowledgeArticleController {

    private final KnowledgeArticleService service;

    /** 分页列表 */
    @GetMapping
    public Result<PageResult<KnowledgeArticle>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Page<KnowledgeArticle> result = service.page(page, size, category, status, keyword);
        return Result.ok(PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords()));
    }

    /** 详情 */
    @GetMapping("/{id}")
    public Result<KnowledgeArticle> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Void> create(@RequestBody KnowledgeArticle article) {
        service.create(article);
        return Result.ok();
    }

    /** 编辑 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody KnowledgeArticle article) {
        article.setId(id);
        service.update(article);
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

    /** 下架 */
    @PutMapping("/{id}/unpublish")
    public Result<Void> unpublish(@PathVariable Long id) {
        service.unpublish(id);
        return Result.ok();
    }
}
