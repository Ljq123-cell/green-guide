package com.greenguide.module.achievement.service;

import com.greenguide.module.achievement.entity.Achievement;
import com.greenguide.module.achievement.entity.UserAchievement;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface AchievementService {

    List<Achievement> listAll();

    List<UserAchievement> getUserAchievements(Long userId);

    Page<Achievement> page(int page, int size);

    Achievement getById(Long id);

    void create(Achievement achievement);

    void update(Achievement achievement);

    void delete(Long id);
}
