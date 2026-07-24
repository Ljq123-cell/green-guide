package com.greenguide.module.user.controller;

import com.greenguide.common.Result;
import com.greenguide.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 用户登录（openId 登录，演示用） */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String openId = body.getOrDefault("openId", "");
        if (openId.isBlank()) {
            return Result.badRequest("openId 不能为空");
        }
        return Result.ok(userService.login(openId));
    }
}
