package com.greenguide.security;

import com.greenguide.module.admin.entity.AdminUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class AdminUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String nickname;
    private final String role;
    private final boolean enabled;

    public AdminUserDetails(AdminUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.enabled = user.getStatus() != null && user.getStatus() == 1;
    }

    /** JWT令牌还原用 — 密码已无需再校验 */
    public AdminUserDetails(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.password = "";
        this.nickname = username;
        this.role = role;
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
}
