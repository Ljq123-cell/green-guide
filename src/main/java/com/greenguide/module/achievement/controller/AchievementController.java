package com.greenguide.module.achievement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.PageResult;
import com.greenguide.common.Result;
import com.greenguide.module.achievement.entity.Achievement;
import com.greenguide.module.achievement.entity.UserAchievement;
import com.greenguide.module.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService service;

    /** 所有成就列表 (公开) */
    @GetMapping("/achievements")
    public Result<List<Achievement>> listAll() {
        return Result.ok(service.listAll());
    }

    /** 后台管理: 成就分页 */
    @GetMapping("/admin/achievement")
    public Result<PageResult<Achievement>> adminList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Achievement> result = service.page(page, size);
        return Result.ok(PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(), result.getRecords()));
    }

    /** 后台管理: 成就详情 */
    @GetMapping("/admin/achievement/{id}")
    public Result<Achievement> detail(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    /** 后台管理: 新增成就 */
    @PostMapping("/admin/achievement")
    public Result<Void> create(@RequestBody Achievement achievement) {
        service.create(achievement);
        return Result.ok();
    }

    /** 后台管理: 编辑成就 */
    @PutMapping("/admin/achievement/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Achievement achievement) {
        achievement.setId(id);
        service.update(achievement);
        return Result.ok();
    }

    /** 后台管理: 删除成就 */
    @DeleteMapping("/admin/achievement/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }

    /** 用户已获得的成就 */
    @GetMapping("/user/{userId}/achievements")
    public Result<List<UserAchievement>> userAchievements(@PathVariable Long userId) {
        return Result.ok(service.getUserAchievements(userId));
    }
}
