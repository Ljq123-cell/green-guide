package com.greenguide.module.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greenguide.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_article")
public class KnowledgeArticle extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String category;
    private String coverImage;
    private String source;
    private String tags;
    private Integer viewCount;
    private String status;
    private LocalDateTime publishedAt;
    private Long createdBy;
}
