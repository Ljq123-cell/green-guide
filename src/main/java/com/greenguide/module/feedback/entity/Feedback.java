package com.greenguide.module.feedback.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greenguide.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_feedback")
public class Feedback extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String feedbackType;
    private String content;
    private String relatedGarbageName;
    private String imageUrl;
    private String status;
    private Long handlerId;
    private String handlerNote;
    private LocalDateTime processedAt;
}
