package com.greenguide.module.garbage.controller;

import com.greenguide.common.Result;
import com.greenguide.module.garbage.entity.GarbageCategory;
import com.greenguide.module.garbage.service.GarbageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/garbage")
@RequiredArgsConstructor
public class GarbageCategoryController {

    private final GarbageCategoryService service;

    /** 搜索垃圾分类 */
    @GetMapping("/search")
    public Result<List<GarbageCategory>> search(@RequestParam(required = false) String keyword) {
        return Result.ok(service.search(keyword));
    }

    /** 按类别获取 */
    @GetMapping
    public Result<List<GarbageCategory>> listByCategory(@RequestParam(required = false) String category) {
        return Result.ok(service.listByCategory(category));
    }

    /** 全部列表 */
    @GetMapping("/all")
    public Result<List<GarbageCategory>> listAll() {
        return Result.ok(service.listAll());
    }

    /** 详情 */
    @GetMapping("/{id}")
    public Result<GarbageCategory> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}
