package com.greenguide.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.dto.LoginDTO;
import com.greenguide.dto.LoginVO;
import com.greenguide.module.admin.entity.AdminUser;
import com.greenguide.module.admin.mapper.AdminUserMapper;
import com.greenguide.module.admin.service.AdminUserService;
import com.greenguide.security.AdminUserDetails;
import com.greenguide.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminUserMapper adminUserMapper;

    @Override
    public LoginVO login(LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        AdminUserDetails userDetails = (AdminUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(
                userDetails.getId(), userDetails.getUsername(), userDetails.getRole());

        // 更新最后登录时间
        AdminUser user = new AdminUser();
        user.setId(userDetails.getId());
        user.setLastLoginTime(LocalDateTime.now());
        adminUserMapper.updateById(user);

        return LoginVO.builder()
                .token(token)
                .nickname(userDetails.getNickname())
                .role(userDetails.getRole())
                .build();
    }
}
