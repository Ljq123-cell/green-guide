package com.greenguide.module.quiz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.greenguide.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("quiz_question")
public class QuizQuestion extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String stem;
    private String questionType;
    private String options;     // JSON string
    private String correctAnswer;
    private String difficulty;
    private String explanation;
    private String categoryTag;
    private Integer usageCount;
    private Integer correctCount;
    private String status;
}
