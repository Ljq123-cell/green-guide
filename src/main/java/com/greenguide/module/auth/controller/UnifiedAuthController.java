package com.greenguide.module.auth.controller;

import com.greenguide.common.Result;
import com.greenguide.dto.LoginDTO;
import com.greenguide.module.auth.service.UnifiedLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class UnifiedAuthController {

    private final UnifiedLoginService unifiedLoginService;

    /** 统一登录：管理员→ADMIN/EDITOR，普通用户→USER */
    @PostMapping("/unified-login")
    public Result<Object> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(unifiedLoginService.login(dto.getUsername(), dto.getPassword()));
    }
}
