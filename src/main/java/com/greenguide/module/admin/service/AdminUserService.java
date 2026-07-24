package com.greenguide.module.admin.service;

import com.greenguide.dto.LoginDTO;
import com.greenguide.dto.LoginVO;

public interface AdminUserService {

    LoginVO login(LoginDTO dto);
}
