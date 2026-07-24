package com.greenguide.module.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.admin.entity.AdminUser;
import com.greenguide.module.admin.mapper.AdminUserMapper;
import com.greenguide.module.user.entity.User;
import com.greenguide.module.user.mapper.UserMapper;
import com.greenguide.security.JwtTokenProvider;
import com.greenguide.dto.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UnifiedLoginService {

    private final AdminUserMapper adminUserMapper;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Object login(String username, String password) {
        // 1. 先尝试管理员登录
        AdminUser admin = adminUserMapper.selectOne(
                new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username));
        if (admin != null) {
            if (admin.getStatus() == 0) {
                throw new BusinessException(403, "账号已被禁用");
            }
            if (passwordEncoder.matches(password, admin.getPassword())) {
                String token = jwtTokenProvider.generateToken(admin.getId(), admin.getUsername(), admin.getRole());
                return LoginVO.builder()
                        .token(token)
                        .nickname(admin.getNickname())
                        .role(admin.getRole())
                        .build();
            }
            throw new BusinessException(401, "密码错误");
        }

        // 2. 尝试普通用户登录（按 openId 或 nickname 匹配）
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getOpenId, username)
                        .or()
                        .eq(User::getNickname, username)
                        .last("LIMIT 1"));
        if (user != null) {
            if (user.getPasswordHash() == null) {
                throw new BusinessException(401, "该用户尚未设置密码，请通过小程序登录");
            }
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                String token = jwtTokenProvider.generateToken(user.getId(), user.getNickname(), "USER");
                return Map.of(
                        "token", token,
                        "userId", user.getId(),
                        "nickname", user.getNickname(),
                        "role", "USER",
                        "totalPoints", user.getTotalPoints() != null ? user.getTotalPoints() : 0
                );
            }
            throw new BusinessException(401, "密码错误");
        }

        throw new BusinessException(404, "用户不存在");
    }
}
