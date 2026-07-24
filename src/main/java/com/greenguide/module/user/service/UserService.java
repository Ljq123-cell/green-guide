package com.greenguide.module.user.service;

import java.util.Map;

public interface UserService {

    /** 通过 openId 登录（为演示简化，自动注册） */
    Map<String, Object> login(String openId);
}
