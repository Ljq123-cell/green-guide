package com.greenguide.module.admin.controller;

import com.greenguide.common.Result;
import com.greenguide.dto.LoginDTO;
import com.greenguide.dto.LoginVO;
import com.greenguide.module.admin.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserService adminUserService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(adminUserService.login(dto));
    }
}
