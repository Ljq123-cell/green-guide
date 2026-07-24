package com.greenguide.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenguide.module.user.entity.User;
import com.greenguide.module.user.mapper.UserMapper;
import com.greenguide.module.user.service.UserService;
import com.greenguide.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public Map<String, Object> login(String openId) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));

        if (user == null) {
            // 新用户自动注册
            user = new User();
            user.setOpenId(openId);
            user.setNickname("用户" + System.currentTimeMillis() % 100000);
            user.setTotalPoints(0);
            user.setTotalQueries(0);
            user.setTotalAnswers(0);
            user.setCorrectAnswers(0);
            user.setConsecutiveDays(0);
            user.setLastLoginDate(LocalDate.now());
            userMapper.insert(user);
        } else {
            // 更新登录日期
            user.setLastLoginDate(LocalDate.now());
            userMapper.updateById(user);
        }

        // 生成 JWT
        String token = jwtTokenProvider.generateToken(user.getId(), user.getNickname(), "USER");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("nickname", user.getNickname());
        result.put("totalPoints", user.getTotalPoints());
        return result;
    }
}
