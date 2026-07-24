package com.greenguide.module.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.learning.entity.LearningRecord;
import com.greenguide.module.learning.entity.UserPoints;
import com.greenguide.module.learning.mapper.LearningRecordMapper;
import com.greenguide.module.learning.mapper.UserPointsMapper;
import com.greenguide.module.learning.service.LearningService;
import com.greenguide.module.user.entity.User;
import com.greenguide.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {

    private final UserMapper userMapper;
    private final LearningRecordMapper recordMapper;
    private final UserPointsMapper pointsMapper;

    @Override
    public Map<String, Object> getUserStats(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 查询记录数
        long totalQueries = recordMapper.selectCount(
                new LambdaQueryWrapper<LearningRecord>().eq(LearningRecord::getUserId, userId));
        long totalAnswers = recordMapper.selectCount(
                new LambdaQueryWrapper<LearningRecord>()
                        .eq(LearningRecord::getUserId, userId)
                        .eq(LearningRecord::getRecordType, "QUIZ"));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPoints", user.getTotalPoints());
        stats.put("totalQueries", totalQueries);
        stats.put("totalAnswers", totalAnswers);
        stats.put("correctAnswers", user.getCorrectAnswers());
        stats.put("consecutiveDays", user.getConsecutiveDays());
        stats.put("accuracy", totalAnswers > 0
                ? Math.round(user.getCorrectAnswers() * 100.0 / totalAnswers) : 0);
        return stats;
    }

    @Override
    @Transactional
    public void recordSearch(Long userId, String garbageName, String category) {
        // 记录学习
        LearningRecord record = new LearningRecord();
        record.setUserId(userId);
        record.setRecordType("SEARCH");
        record.setTargetName(garbageName);
        record.setResultCategory(category);
        recordMapper.insert(record);

        // 积分+1
        UserPoints points = new UserPoints();
        points.setUserId(userId);
        points.setPoints(1);
        points.setReason("SEARCH");
        pointsMapper.insert(points);

        // 更新用户总积分和查询次数
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setTotalPoints(user.getTotalPoints() + 1);
            user.setTotalQueries(user.getTotalQueries() + 1);
            userMapper.updateById(user);
        }
    }
}
