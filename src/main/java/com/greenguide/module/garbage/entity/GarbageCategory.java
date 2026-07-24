package com.greenguide.module.garbage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("garbage_category")
public class GarbageCategory {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private String description;
    private String disposalGuide;
    private String commonExamples;
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
