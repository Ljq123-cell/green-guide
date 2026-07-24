package com.greenguide.module.learning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("learning_record")
public class LearningRecord {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String recordType;
    private Long targetId;
    private String targetName;
    private String resultCategory;
    private LocalDateTime createdAt;
}
