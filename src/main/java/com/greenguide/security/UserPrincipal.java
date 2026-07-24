package com.greenguide.security;

import lombok.Getter;

/** 普通用户 Principal */
@Getter
public class UserPrincipal {

    private final Long id;
    private final String nickname;

    public UserPrincipal(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
