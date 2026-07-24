package com.greenguide.module.achievement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.achievement.entity.Achievement;
import com.greenguide.module.achievement.entity.UserAchievement;
import com.greenguide.module.achievement.mapper.AchievementMapper;
import com.greenguide.module.achievement.mapper.UserAchievementMapper;
import com.greenguide.module.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {

    private final AchievementMapper achievementMapper;
    private final UserAchievementMapper userAchievementMapper;

    @Override
    public List<Achievement> listAll() {
        return achievementMapper.selectList(
                new LambdaQueryWrapper<Achievement>().orderByAsc(Achievement::getSortOrder));
    }

    @Override
    public List<UserAchievement> getUserAchievements(Long userId) {
        return userAchievementMapper.selectList(
                new LambdaQueryWrapper<UserAchievement>().eq(UserAchievement::getUserId, userId));
    }

    @Override
    public Page<Achievement> page(int page, int size) {
        return achievementMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Achievement>().orderByAsc(Achievement::getSortOrder));
    }

    @Override
    public Achievement getById(Long id) {
        Achievement a = achievementMapper.selectById(id);
        if (a == null) {
            throw new BusinessException(404, "成就不存在");
        }
        return a;
    }

    @Override
    public void create(Achievement achievement) {
        achievementMapper.insert(achievement);
    }

    @Override
    public void update(Achievement achievement) {
        Achievement existing = getById(achievement.getId());
        existing.setName(achievement.getName());
        existing.setDescription(achievement.getDescription());
        existing.setIcon(achievement.getIcon());
        existing.setConditionType(achievement.getConditionType());
        existing.setConditionValue(achievement.getConditionValue());
        existing.setPointsReward(achievement.getPointsReward());
        existing.setSortOrder(achievement.getSortOrder());
        achievementMapper.updateById(existing);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        achievementMapper.deleteById(id);
    }
}
