package com.greenguide.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greenguide.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String openId;
    private String unionId;
    private String nickname;
    private String avatarUrl;
    private String passwordHash;
    private Integer totalPoints;
    private Integer totalQueries;
    private Integer totalAnswers;
    private Integer correctAnswers;
    private Integer consecutiveDays;
    private LocalDate lastLoginDate;
}
